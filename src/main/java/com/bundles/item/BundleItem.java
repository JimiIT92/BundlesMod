package com.bundles.item;

import com.bundles.init.BundleResources;
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
        super(new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1).durability(BundleResources.MAX_BUNDLE_ITEMS));
    }

    /**
     * Check if the item is damageable
     *
     * @return False
     */
    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    /**
     * Check if the item can be enchanted with books
     *
     * @param stack Bundle Item Stack
     * @param book Enchantment Book
     * @return False
     */
    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
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
     * Set the Bundle to not bet damaged,
     * so the "Damage"NBT won't be shown
     * in the tooltip
     *
     * @param stack Bundle Item Stack
     * @return False
     */
    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }
}