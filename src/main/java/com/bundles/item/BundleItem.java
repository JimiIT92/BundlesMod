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
        super(new Item.Properties().group(ItemGroup.TOOLS));
    }

    /**
     * Show the Item Stack Durability Bar
     *
     * @param stack Item Stack
     * @return True
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
}
