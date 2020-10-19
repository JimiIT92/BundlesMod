package com.bundles.util;

import com.bundles.init.BundleResources;
import com.bundles.item.BundleItem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
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
     * Check if the Bundle is empty
     *
     * @param bundle Bundle Item Stack
     * @return True if the Bundle is empty, False otherwise
     */
    public static boolean isEmpty(ItemStack bundle) {
        return getBundleItemsCount(bundle) == 0;
    }

    /**
     * Check if an Item Stack can be added to a Bundle
     *
     * @param bundle Bundle Item Stack
     * @param stack Item Stack to add
     * @return True if the Item Stack can be added to a Bundle, False otherwise
     */
    public static boolean canAddItemStackToBundle(ItemStack bundle, ItemStack stack) {
        if(!isBundle(bundle) || isFull(bundle) || isIgnored(stack)) {
            return false;
        }
        ItemStack bundleItemStack = getItemStackFor(bundle, stack);
        return bundleItemStack.isEmpty() || bundleItemStack.getCount() < getMaxStackSizeForBundle(stack);
    }

    /**
     * Check if an Item Stack is for a Container Block
     *
     * @param stack Item Stack
     * @return True if the Item Stack is for a Container Block, False otherwise
     */
    private static boolean isIgnored(ItemStack stack) {
        Item item = stack.getItem();
        if(item instanceof BlockItem) {
            Tag<Block> blockTag = BlockTags.getCollection().get(BundleResources.BUNDLE_IGNORED_BLOCKS_TAG);
            return blockTag != null && blockTag.contains(((BlockItem)item).getBlock());
        }
        Tag<Item> itemTag = ItemTags.getCollection().get(BundleResources.BUNDLE_IGNORED_ITEMS_TAG);
        return itemTag != null && itemTag.contains(item);
    }

    /**
     * Add an Item Stack to a Bundle
     *
     * @param bundle Bundle Item Stack
     * @param stack Item Stack to add
     * @param player Player
     */
    public static void addItemStackToBundle(ItemStack bundle, ItemStack stack, PlayerEntity player) {
        if(!isBundle(bundle) || isFull(bundle) || isBundle(stack)) {
            return;
        }
        ItemStack stackToAdd = stack.copy();
        int maxItemsToAdd = bundle.getMaxDamage() - getBundleItemsCount(bundle);
        stackToAdd.setCount(Math.min(getMaxStackSizeForBundleToInsert(stackToAdd), maxItemsToAdd));
        CompoundNBT bundleTag = bundle.getOrCreateTag();
        ListNBT items = bundleTag.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, Constants.NBT.TAG_COMPOUND);
        CompoundNBT itemStackNbt = new CompoundNBT();
        ItemStack stackFromBundle = getItemStackFor(bundle, stackToAdd);
        int index = getItemStackIndex(bundle, stackFromBundle);
        if(!stackFromBundle.isEmpty()) {
            stackToAdd.setCount(Math.min(stackToAdd.getCount(), getMaxStackSizeForBundle(stack) - stackFromBundle.getCount()));
            stackFromBundle.setCount(stackFromBundle.getCount() + stackToAdd.getCount());
        }
        if(index != -1) {
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
    }

    /**
     * Empty a Bundle
     *
     * @param bundle Bundle
     * @param player Player
     */
    public static void emptyBundle(ItemStack bundle, PlayerEntity player) {
        if(!isBundle(bundle) || isEmpty(bundle)) {
            return;
        }
        getItemsFromBundle(bundle).forEach(item -> {
            if(!player.addItemStackToInventory(item)) {
                if(!player.isCreative()) {
                    player.dropItem(item, true);
                }
            }
            else if(item.getCount() > 0) {
                if(!player.isCreative()) {
                    player.dropItem(item, true);
                }
            }
        });
        CompoundNBT bundleTag = bundle.getOrCreateTag();
        ListNBT items = bundleTag.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, Constants.NBT.TAG_COMPOUND);
        items.clear();
        bundleTag.put(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, items);
        bundle.setTag(bundleTag);
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
     * @param stack Item Stack
     * @return Item Stack for the Item or Empty Item Stack if not found
     */
    private static ItemStack getItemStackFor(ItemStack bundle, ItemStack stack) {
        return getItemsFromBundle(bundle).stream().filter(x -> ItemStack.areItemStacksEqual(x, stack)).findFirst().orElse(ItemStack.EMPTY);
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
}
