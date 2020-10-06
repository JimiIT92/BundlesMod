package com.bundles.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * Bundle Item
 *
 * @author JimiIT92
 */
public class BundleItem extends Item {

    /**
     * Constructor. Set the Bundle Item properties
     */
    public BundleItem() {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).defaultMaxDamage(0));
    }

    /**
     * Show the Item Stack Durability Bar
     *
     * @param stack Item Stack
     * @return True
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getDurabilityForDisplay(stack) > 0;
    }

    /**
     * Get the Durability size based
     * on how many items are in the Bundle
     *
     * @param stack Item Stack
     * @return Stack Durability
     */
    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 32;
    }

    /**
     * Get the Durability Bar Color
     *
     * @param stack Item Stack
     * @return Durability Bar Color
     */
    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0x0C91FF;
    }

    /**
     * Get the maximum number of items
     * that can fit inside a Bundle
     *
     * @param stack Item Stack
     * @return Maximum number of items per Bundle
     */
    @Override
    public int getMaxDamage(ItemStack stack) {
        return 64;
    }

    /**
     * Check if the Bundle is full
     *
     * @param stack Item Stack
     * @return True if the Bundle is full, False otherwise
     */
    public boolean isFull(ItemStack stack) {
        return false;
    }
}
