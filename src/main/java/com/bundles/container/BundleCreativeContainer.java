package com.bundles.container;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Bundle Creative Container
 *
 * @author JimiIT92
 */
public class BundleCreativeContainer extends CreativeScreen.CreativeContainer {

    /**
     * Constructor. Initialize the Container
     *
     * @param player Player
     */
    public BundleCreativeContainer(PlayerEntity player) {
        super(player);
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
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
}
