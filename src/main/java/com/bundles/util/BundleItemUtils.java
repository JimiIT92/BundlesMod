package com.bundles.util;

import com.bundles.init.BundleItems;
import net.minecraft.item.ItemStack;

/**
 * Bundle Item Utilities
 *
 * @author JimiIT92
 */
public final class BundleItemUtils {

    /**
     * Check if the Item Stack is a Bundle Item Stack
     *
     * @param stack Item Stack
     * @return True if is a Bundle Item Stack, False otherwise
     */
    public static boolean isBundle(ItemStack stack) {
        return stack.getItem() == BundleItems.BUNDLE.get();
    }

    /**
     * Check if a Bundle is full
     *
     * @param stack Bundle Item Stack
     * @return True if the Bundle is full, False otherwise
     */
    public static boolean isFull(ItemStack stack) {
        return stack.getDamage() == 0;
    }
}
