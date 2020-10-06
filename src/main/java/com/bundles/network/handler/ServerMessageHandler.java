package com.bundles.network.handler;

import com.bundles.Bundles;
import com.bundles.capability.DraggedItemCapability;
import com.bundles.init.BundleCapabilities;
import com.bundles.network.message.DraggedItemClientMessage;
import com.bundles.network.message.DraggedItemServerMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

/**
 * Server Message Handler
 *
 * @author JimiIT92
 */
public class ServerMessageHandler {

    /**
     * Check if the Protocol is accepted by the Server
     *
     * @param protocolVersion Protocol Version
     * @return True if the Protocol is accepted, False otherwise
     */
    public static boolean isThisProtocolAcceptedByServer(String protocolVersion) {
        return Bundles.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }

    /**
     * Handle messages
     *
     * @param message Message
     * @param ctxSupplier Context Supplier
     */
    public static void onMessageReceived(final DraggedItemServerMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context context = ctxSupplier.get();
        LogicalSide side = context.getDirection().getReceptionSide();
        context.setPacketHandled(true);

        if(!side.isServer()) {
            return;
        }

        final ServerPlayerEntity playerEntity = context.getSender();
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
    private static void processMessage(DraggedItemServerMessage message, ServerPlayerEntity playerEntity) {
        DraggedItemCapability capability = BundleCapabilities.getDraggedItemCapability(playerEntity);
        if(capability != null) {
            DraggedItemClientMessage clientMessage = new DraggedItemClientMessage(message.draggedItemStack);
            Bundles.NETWORK.send(PacketDistributor.PLAYER.with(() -> playerEntity), clientMessage);
            capability.setDraggedItemStack(message.draggedItemStack);
        }
    }
}
