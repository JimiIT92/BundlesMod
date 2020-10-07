package com.bundles.util;

import com.bundles.item.BundleItem;
import net.minecraft.item.ItemStack;

/**
 * Bundle Container Utilities
 *
 * @author JimiIT92
 */
public final class BundleContainerUtils {

    /**
     * Check if an Item Stack is a Bundle
     *
     * @param stack Item Stack
     * @return True if the Item Stack is a Bundle, False otherwise
     */
    public static boolean isBundle(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.getItem() instanceof BundleItem;
    }

    /**
     * Empty a Bundle
     *
     * @param stack Bundle Item Stack
     */
    public static void emptyBundle(ItemStack stack) {

    }
}
