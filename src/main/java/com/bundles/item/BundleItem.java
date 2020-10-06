package com.bundles.item;

import com.bundles.Bundles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Bundle Item
 *
 * @author JimiIT92
 */
public class BundleItem extends Item {

    /**
     * NBT Key for Full State
     */
    private final String FULL_NBT_KEY = Bundles.MOD_ID + ":full";

    /**
     * Constructor. Set the Bundle Item properties
     */
    public BundleItem() {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(64));
    }

    /**
     * Show the Item Stack Durability Bar
     *
     * @param stack Item Stack
     * @return True
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getDamage() > 0;
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
     * Check if the Bundle is full
     *
     * @param stack Bundle Item Stack
     * @return True if the Bundle is full, False otherwise
     */
    public boolean isFull(ItemStack stack) {
        return stack.hasTag() && stack.getTag() != null && stack.getTag().getBoolean(FULL_NBT_KEY);
    }

    /**
     * Set the Bundle full
     *
     * @param stack Bundle Item Stack
     * @param full If the Bundle is full
     */
    public void setFull(ItemStack stack, boolean full) {
        if(!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }
        assert stack.getTag() != null;
        stack.getTag().putBoolean(FULL_NBT_KEY, full);
    }

    /**
     * Increase the Item Count inside the Bundle
     *
     * @param bundle Bundle Item Stack
     * @param count How many Items to add
     */
    public void addItems(ItemStack bundle, int count) {
        boolean isFull = this.isFull(bundle);
        if(!isFull) {
            int damage = bundle.getDamage() == 0 ? bundle.getMaxDamage() : bundle.getDamage();
            bundle.setDamage(damage - count);
            if(bundle.getDamage() == 0) {
                this.setFull(bundle, true);
            }
        }
    }

    /**
     * Empty all Items inside the Bundle
     *
     * @param bundle Bundle Item Stack
     */
    public void emptyBundle(ItemStack bundle) {
        bundle.setDamage(0);
        this.setFull(bundle, false);
    }
}
