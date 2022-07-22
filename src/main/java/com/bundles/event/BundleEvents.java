package com.bundles.event;

import com.bundles.init.BundleResources;
import com.bundles.item.BundleItem;
import com.bundles.network.BundleMessage;
import com.bundles.tooltip.BundleTooltip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.EntityLeaveWorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Bundle Event Listeners
 */
@Mod.EventBusSubscriber(modid = BundleResources.MOD_ID)
public final class BundleEvents {

    /**
     * Fill or release an Item from a Bundle when
     * the player clicks inside an Inventory
     *
     * @param event Mouse Clicked Event
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onInventorySlotInteract(final GuiScreenEvent.MouseClickedEvent event) {
        if(!event.isCanceled() && event.getGui() instanceof ContainerScreen<?>) {
            ContainerScreen<?> containerScreen = (ContainerScreen<?>)event.getGui();
            Slot slot = containerScreen.getSlotUnderMouse();
            PlayerEntity player = Minecraft.getInstance().player;
            int button = event.getButton();
            if(slot != null && !(slot instanceof CraftingResultSlot) && slot.isActive() && player != null && button == 1) {
                ItemStack carriedItemStack = player.inventory.getCarried();
                ItemStack slotStack = slot.getItem();
                boolean sendServerMessage = !player.isCreative() || !(containerScreen instanceof CreativeScreen);
                if(carriedItemStack.getItem() instanceof BundleItem) {
                    if(sendServerMessage) {
                        BundleResources.NETWORK.sendToServer(new BundleMessage(carriedItemStack, slotStack, slot.index, button, true));
                    } else {
                        ((BundleItem)carriedItemStack.getItem()).overrideStackedOnOther(carriedItemStack, slot, player, false);
                    }
                } else if(slotStack.getItem() instanceof BundleItem) {
                    if(sendServerMessage) {
                        BundleResources.NETWORK.sendToServer(new BundleMessage(slotStack, carriedItemStack, slot.index, button, false));
                    } else {
                        ((BundleItem)slotStack.getItem()).overrideOtherStackedOnMe(slotStack, carriedItemStack, slot, player, false);
                    }
                }
            }
        }
    }

    /**
     * Drop Bundle Contents when is destroyed
     *
     * @param event Entity Leave World Event
     */
    @SubscribeEvent
    public static void onBundleDestroyed(final EntityLeaveWorldEvent event) {
        if(!event.isCanceled()) {
            Entity entity = event.getEntity();
            if(entity instanceof ItemEntity) {
                ItemStack itemStack = ((ItemEntity)entity).getItem();
                Item item = itemStack.getItem();
                if(item instanceof BundleItem) {
                    World world = entity.level;
                    if (!world.isClientSide) {
                        BundleItem.getContents(itemStack).forEach(stack -> world.addFreshEntity(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), stack)));
                    }
                }
            }
        }
    }

    /**
     * Draw the Bundle Tooltip UI
     *
     * @param event Render Tooltip Event
     */
    //@SubscribeEvent
    public static void onRenderTooltip(final RenderTooltipEvent.Pre event) {
        if(!event.isCanceled() && event.getStack().getItem() instanceof BundleItem) {
            event.setCanceled(true);
            BundleTooltip.draw(event);
        }
    }

}