package com.bundles.util;

import com.bundles.init.BundleResources;
import com.bundles.item.BundleItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if(!isBundle(bundle) || isFull(bundle)) {
            return false;
        }
        ItemStack bundleItemStack = getItemStackFor(bundle, stack.getItem());
        int maxStackSize = stack.getMaxStackSize();
        return bundleItemStack.isEmpty() || maxStackSize == 1
                || (maxStackSize > 1 && bundleItemStack.getCount() < (maxStackSize / 2));
    }

    /**
     * Add an Item Stack to a Bundle
     *
     * @param bundle Bundle Item Stack
     * @param stack Item Stack to add
     */
    public static void addItemStackToBundle(ItemStack bundle, ItemStack stack) {
        if(!isBundle(bundle) || isFull(bundle)) {
            return;
        }
        CompoundNBT bundleTag = bundle.getOrCreateTag();
        ListNBT items = bundleTag.getList(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, Constants.NBT.TAG_COMPOUND);
        CompoundNBT itemStackNbt = new CompoundNBT();
        stack.write(itemStackNbt);
        items.add(itemStackNbt);
        bundleTag.put(BundleResources.BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION, items);
        bundle.setTag(bundleTag);
    }

    /**
     * Empty a Bundle
     *
     * @param bundle Bundle
     */
    public static void emptyBundle(ItemStack bundle, PlayerEntity player) {
        if(!isBundle(bundle)) {
            return;
        }
        getItemsFromBundle(bundle).forEach(player::addItemStackToInventory);
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
     * @param item Item to get the Item Stack
     * @return Item Stack for the Item or Empty Item Stack if not found
     */
    public static ItemStack getItemStackFor(ItemStack bundle, Item item) {
        return getItemsFromBundle(bundle).stream().filter(x -> x.getItem().equals(item)).findFirst().orElse(ItemStack.EMPTY);
    }
}
