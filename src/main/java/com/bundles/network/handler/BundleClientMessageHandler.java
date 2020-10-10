package com.bundles.network.handler;

import com.bundles.init.BundleResources;
import com.bundles.network.message.BundleClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @author JimiIT92
 */
public class BundleClientMessageHandler {

    /**
     * Check if the Protocol is accepted by the Server
     *
     * @param protocolVersion Protocol Version
     * @return True if the Protocol is accepted, False otherwise
     */
    public static boolean isThisProtocolAcceptedByClient(String protocolVersion) {
        return BundleResources.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }
    /**
     * Handle messages
     *
     * @param message Message
     * @param ctxSupplier Context Supplier
     */
    public static void onMessageReceived(final BundleClientMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context context = ctxSupplier.get();
        LogicalSide side = context.getDirection().getReceptionSide();
        context.setPacketHandled(true);

        if(!side.isClient()) {
            return;
        }

        ClientPlayerEntity playerEntity = Minecraft.getInstance().player;
        if(playerEntity == null) {
            return;
        }

        context.enqueueWork(() -> processMessage(message, playerEntity));
    }

    /**
     * Process the Message
     *
     * @param message Message
     * @param playerEntity Player
     */
    private static void processMessage(BundleClientMessage message, ClientPlayerEntity playerEntity) {
        Container container = playerEntity.openContainer;
        Slot slot = container.getSlot(message.slotId);
        slot.putStack(message.slotStack);
        if(!message.empty) {
            playerEntity.inventory.setItemStack(message.bundle);
        }
    }

}
