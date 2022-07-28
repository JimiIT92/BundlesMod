package com.bundles.network.handler;

import com.bundles.init.BundleResources;
import com.bundles.item.BundleItem;
import com.bundles.network.BundleMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Server Message Handler for a Bundle Message
 */
public final class BundleServerMessageHandler {

    /**
     * Check if the Protocol is accepted by the Server
     *
     * @param protocolVersion Protocol Version
     * @return True if the Protocol is accepted, False otherwise
     */
    public static boolean isThisProtocolAcceptedByServer(String protocolVersion) {
        return BundleResources.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }

    /**
     * Handle the Bundle Message
     *
     * @param message Message
     * @param ctxSupplier Context Supplier
     */
    public static void onMessageReceived(final BundleMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context context = ctxSupplier.get();
        LogicalSide side = context.getDirection().getReceptionSide();
        context.setPacketHandled(true);

        if(!side.isServer()) {
            return;
        }

        ServerPlayerEntity playerEntity = context.getSender();
        if(playerEntity == null) {
            return;
        }

        context.enqueueWork(() -> processMessage(message, playerEntity));
    }

    /**
     * Process the Bundle Message
     *
     * @param message Message
     * @param player Player
     */
    private static void processMessage(BundleMessage message, ServerPlayerEntity player) {
        Container container = player.containerMenu;
        Slot slot = container.slots.stream().filter(x -> x.index == message.slotId).findFirst().orElse(container.getSlot(message.slotId));
        ItemStack bundle = message.bundle;
        ItemStack slotStack = message.item;
        Map.Entry<ItemStack, ItemStack> bundleAndSlotStack;
        if(message.stackOnOther) {
            bundleAndSlotStack = ((BundleItem)bundle.getItem()).overrideStackedOnOther(bundle, slot, player, true);
        } else {
            bundleAndSlotStack = ((BundleItem)bundle.getItem()).overrideOtherStackedOnMe(bundle, slotStack, slot, player, true);
        }
        bundle = bundleAndSlotStack.getKey();
        ItemStack slotStackResult = bundleAndSlotStack.getValue();
        boolean isAir = slotStackResult.getItem().equals(Items.AIR) && slotStackResult.getCount() != 0;
        if(!isAir) {
            int slotStackCount = slotStackResult.getCount();
            if(slotStackCount <= 0) {
                slot.set(bundle);
                player.inventory.setCarried(ItemStack.EMPTY);
            } else {
                slotStack.setCount(slotStackCount);
                slot.set(slotStack);
                player.inventory.setCarried(bundle);
            }
        }
        player.refreshContainer(player.containerMenu);
    }
}