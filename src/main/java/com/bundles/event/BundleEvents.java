package com.bundles.event;

import com.bundles.Bundles;
import com.bundles.capability.DraggedItemCapability;
import com.bundles.init.BundleCapabilities;
import com.bundles.init.BundleItems;
import com.bundles.item.BundleItem;
import com.bundles.network.message.DraggedItemServerMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Bundle Events
 *
 * @author JimiIT92
 */
public final class BundleEvents {

    //Left click -  on bundle:  drag
    //              on stack:   increase bundle size if not curse of binding, bundle, ender chest or shulker box
    //              on empty:   pose bundle

    /**
     * Bundle Events
     *
     * @param event Mouse Click Event
     */
    @SubscribeEvent
    public static void onBundleClick(final GuiScreenEvent.MouseClickedEvent event) {
        if(!event.isCanceled() && event.getGui() instanceof ContainerScreen) {
            ContainerScreen<?> containerScreen = (ContainerScreen<?>)event.getGui();
            boolean isCreativeContainer = containerScreen.getContainer() instanceof CreativeScreen.CreativeContainer;
            if(containerScreen.getContainer() instanceof PlayerContainer || isCreativeContainer) {
                Slot slot = containerScreen.getSlotUnderMouse();
                PlayerEntity player = Minecraft.getInstance().player;
                if(slot != null && slot.isEnabled() && player != null) {
                    ItemStack stack = slot.getStack();
                    DraggedItemCapability capability = BundleCapabilities.getDraggedItemCapability(player);
                    if(capability != null) {
                        System.out.println("BEFORE: " + capability.getDraggedItemStack());
                        Bundles.NETWORK.sendToServer(new DraggedItemServerMessage(stack));
                        System.out.println("AFTER: " + capability.getDraggedItemStack());
                    } else {
                        System.out.println("NO CAPABILITY");
                    }
                    if(!stack.isEmpty()) {

                        switch (event.getButton()) {
                            case 0:
                            default:
                                break;
                            case 1:
                                if(isBundle(stack)) {
                                    emptyBundle(stack);
                                    event.setCanceled(true);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Check if an Item Stack is a Bundle
     *
     * @param stack Item Stack
     * @return True if the Item Stack is a Bundle, False otherwise
     */
    private static boolean isBundle(ItemStack stack) {
        return stack.getItem() == BundleItems.BUNDLE.get();
    }

    /**
     * Empty the Bundle Content
     *
     * @param stack Bundle Item Stack
     */
    private static void emptyBundle(ItemStack stack) {
        ((BundleItem)stack.getItem()).emptyBundle(stack);
    }


}
