package com.bundles.item;

import com.bundles.util.BundleItemUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
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
        super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(64));
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
     * Show the Items inside the Bundle on the Tooltip
     * @param stack Bundle Item Stack
     * @param worldIn World
     * @param tooltip Tooltip List
     * @param flagIn Tooltip flag
     */
    @ParametersAreNonnullByDefault
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        List<ItemStack> bundleItems = BundleItemUtils.getItemsFromBundle(stack);
        bundleItems.forEach(x -> {
            IFormattableTextComponent iformattabletextcomponent = x.getDisplayName().func_230532_e_();
            iformattabletextcomponent.func_240702_b_(" x").func_240702_b_(String.valueOf(x.getCount()));
            tooltip.add(iformattabletextcomponent);
        });
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
