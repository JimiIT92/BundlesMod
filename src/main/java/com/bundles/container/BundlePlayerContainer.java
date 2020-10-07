package com.bundles.container;

import com.bundles.util.BundleItemUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Bundle Player Container
 *
 * @author JimiIT92
 */
public class BundlePlayerContainer extends PlayerContainer {

    /**
     * Constructor. Initialize the Container
     *
     * @param playerIn Player
     * @param isLocalWorld If is a local world
     */
    public BundlePlayerContainer(PlayerEntity playerIn, boolean isLocalWorld) {
        super(playerIn.inventory, isLocalWorld, playerIn);
    }

    /**
     * Handle mouse click on a slot
     *
     * @param slotId Slot ID
     * @param dragType Drag Type
     * @param clickTypeIn Click Type
     * @param player Player
     * @return Item Stack to put inside the slot
     */
    @ParametersAreNonnullByDefault
    @MethodsReturnNonnullByDefault
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        ItemStack slotItemStack = getSlot(slotId).getStack();
        ItemStack draggedItemStack = player.inventory.getItemStack();
        System.out.println(slotItemStack);
        System.out.println(draggedItemStack);
        if(dragType == 1 && clickTypeIn.equals(ClickType.PICKUP) && BundleItemUtils.isBundle(slotItemStack)) {
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
}
