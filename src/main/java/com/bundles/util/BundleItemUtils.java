package com.bundles.util;

import com.bundles.init.BundleResources;
import com.bundles.item.BundleItem;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Bundle Item Utilities
 *
 * @author JimiIT92
 */
public final class BundleItemUtils {

    /**
     * Check if the Item Stack is a Bundle Item Stack
     *
     * @param bundle Item Stack
     * @return True if is a Bundle Item Stack, False otherwise
     */
    public static boolean isBundle(ItemStack bundle) {
        return bundle.getItem() instanceof BundleItem;
    }

    /**
     * Check if a Bundle is full
     *
     * @param bundle Bundle Item Stack
     * @return True if the Bundle is full, False otherwise
     */
    public static boolean isFull(ItemStack bundle) {
        return getBundleItemsCount(bundle) >= bundle.getMaxDamage();
    }

    /**
     * Check if an Item Stack can be added to a Bundle
     *
     * @param bundle Bundle Item Stack
     * @param stack Item Stack to add
     * @return True if the Item Stack can be added to a Bundle, False otherwise
     */
    public static boolean canAddItemStackToBundle(ItemStack bundle, ItemStack stack) {
        if(!isBundle(bundle) || isFull(bundle) || isContainer(stack)) {
            return false;
        }
        ItemStack bundleItemStack = getItemStackFor(bundle, stack.getItem());
        return bundleItemStack.isEmpty() || bundleItemStack.getCount() < getMaxStackSizeForBundle(stack);
    }

    /**
     * Check if an Item Stack is for a Container Block
     *
     * @param stack Item Stack
     * @return True if the Item Stack is for a Container Block, False otherwise
     */
    private static boolean isContainer(ItemStack stack) {
        if(stack.getItem() instanceof BlockItem) {
            BlockItem blockItem = (BlockItem)stack.getItem();
            Block block = blockItem.getBlock();
            if(block.hasTileEntity(block.getDefaultState())) {
                TileEntity tileEntity = block.createTileEntity(block.getDefaultState(), null);
                return tileEntity instanceof LockableLootTileEntity;
            }
        }
        return false;
    }

    /**
     * Add an Item Stack to a Bundle
     *
     * @param bundle Bundle Item Stack
     * @param stack Item Stack to add
     * @param player Player
     * @param container Container
     */
    public static void addItemStackToBundle(ItemStack bundle, ItemStack stack, PlayerEntity player, Container container) {
        if(!isBundle(bundle) || isFull(bundle) || isBundle(stack)) {
            return;
        }
        ItemStack stackToAdd = stack.copy();
        int maxItemsToAdd = bundle.getMaxDamage() - getBundleItemsCount(bundle);
        stackToAdd.setCount(Math.min(getMaxStackSizeForBundleToInsert(stackToAdd), maxItemsToAdd));
        CompoundNBT bundleTag = bundle.getOrCreateTag();
        ListNBT items = bundleTag.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, Constants.NBT.TAG_COMPOUND);
        CompoundNBT itemStackNbt = new CompoundNBT();
        ItemStack stackFromBundle = getItemStackFor(bundle, stackToAdd.getItem());
        int index = getItemStackIndex(bundle, stackFromBundle);
        if(!stackFromBundle.isEmpty()) {
            stackToAdd.setCount(Math.min(stackToAdd.getCount(), getMaxStackSizeForBundle(stack) - stackFromBundle.getCount()));
        }
        if(index != -1) {
            stackFromBundle.setCount(stackFromBundle.getCount() + stackToAdd.getCount());
            stackFromBundle.write(itemStackNbt);
            items.remove(index);
            items.add(index, itemStackNbt);
        } else {
            stackToAdd.write(itemStackNbt);
            items.add(itemStackNbt);
        }
        bundleTag.put(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, items);
        bundle.setTag(bundleTag);
        stack.setCount(stack.getCount() - stackToAdd.getCount());
        bundle.setDamage(bundle.getMaxDamage() - getBundleItemsCount(bundle));
        player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
    }

    /**
     * Empty a Bundle
     *
     * @param bundle Bundle
     * @param player Player
     * @param container Container
     */
    public static void emptyBundle(ItemStack bundle, PlayerEntity player, Container container) {
        if(!isBundle(bundle) || getBundleItemsCount(bundle) == 0) {
            return;
        }
        getItemsFromBundle(bundle).forEach(player::addItemStackToInventory);
        CompoundNBT bundleTag = bundle.getOrCreateTag();
        ListNBT items = bundleTag.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, Constants.NBT.TAG_COMPOUND);
        items.clear();
        bundleTag.put(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, items);
        bundle.setTag(bundleTag);
        bundle.setDamage(0);
        player.playSound(SoundEvents.BLOCK_WOOL_BREAK, 1.0F, 1.0F);
    }

    /**
     * Get how many Items are inside the Bundle
     *
     * @param bundle Bundle Item Stack
     * @return Bundle Items Count
     */
    public static int getBundleItemsCount(ItemStack bundle) {
        return Objects.requireNonNull(getItemsFromBundle(bundle)).stream().mapToInt(ItemStack::getCount).sum();
    }

    /**
     * Get the Item Stacks inside the Bundle
     *
     * @param bundle Bundle Item Stack
     * @return Bundle's Item Stacks
     */
    public static List<ItemStack> getItemsFromBundle(ItemStack bundle) {
        if(!isBundle(bundle)) {
            return Collections.emptyList();
        }
        CompoundNBT bundleTag = bundle.getOrCreateTag();
        ListNBT items = bundleTag.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, Constants.NBT.TAG_COMPOUND);
        return items.stream().map(x -> ItemStack.read((CompoundNBT) x)).collect(Collectors.toList());
    }

    /**
     * Get the Item Stack for an Item
     *
     * @param bundle Bundle Item Stack
     * @param item Item to get the Item Stack
     * @return Item Stack for the Item or Empty Item Stack if not found
     */
    private static ItemStack getItemStackFor(ItemStack bundle, Item item) {
        return getItemsFromBundle(bundle).stream().filter(x -> x.getItem().equals(item)).findFirst().orElse(ItemStack.EMPTY);
    }

    /**
     * Get the Item Stack index inside the Bundle
     *
     * @param bundle Bundle Item Stack
     * @param stack Item Stack to find
     * @return Item Stack index
     */
    private static int getItemStackIndex(ItemStack bundle, ItemStack stack) {
        List<ItemStack> items = getItemsFromBundle(bundle);
        return IntStream.range(0, items.size())
                .filter(i -> stack.equals(items.get(i), false))
                .findFirst().orElse(-1);
    }

    /**
     * Get the max stack size for an Item Stack
     * to be put inside a Bundle
     *
     * @param stack Item Stack
     * @return Max stack size for a Bundle
     */
    private static int getMaxStackSizeForBundleToInsert(ItemStack stack) {
        return Math.max(1, Math.min(stack.getCount(), stack.getMaxStackSize()/2));
    }

    /**
     * Get the max stack size allowed inside
     * a Bundle for an Item
     *
     * @param stack Item Stack
     * @return Max Item Stack size inside the Bundle
     */
    private static int getMaxStackSizeForBundle(ItemStack stack) {
        return Math.max(1, stack.getMaxStackSize()/2);
    }

    /**
     * Check if the Container is valid for using a Bundle
     *
     * @param container Container
     * @return True if the Container is valid for using a Bundle, False otherwise
     */
    public static boolean isValidContainerForBundle(Container container) {
        return container instanceof PlayerContainer || container instanceof CreativeScreen.CreativeContainer;
    }
}
