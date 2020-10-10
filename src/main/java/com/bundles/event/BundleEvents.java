package com.bundles.event;

import com.bundles.init.BundleResources;
import com.bundles.network.message.BundleServerMessage;
import com.bundles.util.BundleItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.lang.reflect.Field;

/**
 * Bundle Events
 *
 * @author JimiIT92
 */
public final class BundleEvents {

    /**
     * Handle mouse clicks on Containers
     * to determine if an Item Stack should
     * be put inside a Bundle
     *
     * @param event Mouse Released Event
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMouseReleased(final GuiScreenEvent.MouseReleasedEvent event) {
        if(!event.isCanceled() && event.getGui() instanceof ContainerScreen<?>) {
            ContainerScreen<?> containerScreen = (ContainerScreen<?>)event.getGui();
            if(!BundleItemUtils.isValidContainerForBundle(containerScreen.getContainer())) {
                return;
            }

            Slot slot = containerScreen.getSlotUnderMouse();
            if(slot != null && !(slot instanceof CraftingResultSlot)) {
                PlayerEntity player = Minecraft.getInstance().player;
                if(player != null) {
                    ItemStack draggedItemStack = player.inventory.getItemStack();
                    ItemStack slotStack = slot.getStack();
                    Container container = containerScreen.getContainer();
                    if(slot.canTakeStack(player) && slot.isEnabled()
                            && container.canMergeSlot(draggedItemStack, slot)
                            && slot.isItemValid(draggedItemStack)
                            && slot.getHasStack() && event.getButton() == 0
                            && BundleItemUtils.isBundle(draggedItemStack)
                            && BundleItemUtils.canAddItemStackToBundle(draggedItemStack, slotStack)) {
                        try {
                            Field slotIndexField = Slot.class.getDeclaredField("slotIndex");
                            slotIndexField.setAccessible(true);
                            int slotIndex = (int)slotIndexField.get(slot);
                            BundleResources.NETWORK.sendToServer(new BundleServerMessage(draggedItemStack, slotIndex, false));
                            event.setResult(Event.Result.DENY);
                            event.setCanceled(true);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Handle mouse clicks on Containers
     * to determine if a Bundle should be cleared
     *
     * @param event Mouse Clicked Event
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMouseClick(final GuiScreenEvent.MouseClickedEvent event) {
        if(!event.isCanceled() && event.getGui() instanceof ContainerScreen<?>) {
            ContainerScreen<?> containerScreen = (ContainerScreen<?>)event.getGui();
            if(!BundleItemUtils.isValidContainerForBundle(containerScreen.getContainer())) {
                return;
            }
            Slot slot = containerScreen.getSlotUnderMouse();
            if(slot != null && !(slot instanceof CraftingResultSlot)) {
                PlayerEntity player = Minecraft.getInstance().player;
                if(player != null) {
                    ItemStack slotStack = slot.getStack();
                    if(slot.canTakeStack(player) && slot.isEnabled()
                            && slot.getHasStack() && event.getButton() == 1
                            && BundleItemUtils.isBundle(slotStack)) {
                        try {
                            Field slotIndexField = Slot.class.getDeclaredField("slotIndex");
                            slotIndexField.setAccessible(true);
                            int slotIndex = (int)slotIndexField.get(slot);
                            BundleResources.NETWORK.sendToServer(new BundleServerMessage(slotStack, slotIndex, true));
                            event.setResult(Event.Result.DENY);
                            event.setCanceled(true);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Draw the Bundle Tooltip
     *
     * @param event Draw Screen Event
     */
    @SubscribeEvent
    public static void onToolTipRender(final GuiScreenEvent.DrawScreenEvent event) {
        if(event.getGui() instanceof ContainerScreen<?>) {
            ContainerScreen<?> containerScreen = (ContainerScreen<?>)event.getGui();
            Slot slot = containerScreen.getSlotUnderMouse();
            if(slot != null && BundleItemUtils.isBundle(slot.getStack()) && BundleItemUtils.isEmpty(slot.getStack())) {
                //System.out.println("DRAW BUNDLE TOOLTIP");
            }
        }
    }
}
