package com.bundles.item;

import com.bundles.init.BundleResources;
import com.bundles.util.BundleItemUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

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
        super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(BundleResources.MAX_BUNDLE_ITEMS));
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
     * Determine if the durability bar must be shown
     *
     * @param stack Bundle Item Stack
     * @return True if the Bundle is not empty and is not full, False otherwise
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return !BundleItemUtils.isEmpty(stack) && !BundleItemUtils.isFull(stack);
    }

    /**
     * Get the "Damage" of the Bundle Item Stack
     * This determine the charge of the durability bar
     *
     * @param stack Bundle Item Stack
     * @return Bundle "Damage"
     */
    @Override
    public int getDamage(ItemStack stack) {
        return getMaxDamage(stack) - BundleItemUtils.getBundleItemsCount(stack);
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

    /**
     * Show the Items inside the Bundle on the Tooltip
     * @param stack Bundle Item Stack
     * @param worldIn World
     * @param tooltip Tooltip List
     * @param flagIn Tooltip flag
     */
    @ParametersAreNonnullByDefault
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        BundleItemUtils.getItemsFromBundle(stack).forEach(x -> tooltip.add(x.getDisplayName()
                    .func_230532_e_()
                    .func_240702_b_(" x")
                    .func_240702_b_(String.valueOf(x.getCount()))
                )
        );
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
