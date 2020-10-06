package com.bundles.network.handler;

import com.bundles.Bundles;
import com.bundles.capability.DraggedItemCapability;
import com.bundles.init.BundleCapabilities;
import com.bundles.network.message.DraggedItemClientMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Client Message Handler
 *
 * @author JimiIT92
 */
public class ClientMessageHandler {

    /**
     * Check if the Protocol is accepted by the Client
     *
     * @param protocolVersion Protocol Version
     * @return True if the Protocol is accepted, False otherwise
     */
    public static boolean isThisProtocolAcceptedByClient(String protocolVersion) {
        return Bundles.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }

    /**
     * Handle messages
     *
     * @param message Message
     * @param ctxSupplier Context Supplier
     */
    public static void onMessageReceived(final DraggedItemClientMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context context = ctxSupplier.get();
        LogicalSide side = context.getDirection().getReceptionSide();
        context.setPacketHandled(true);

        if(!side.isClient()) {
            return;
        }

        Optional<ClientWorld> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(side);
        if(!clientWorld.isPresent()) {
            return;
        }

        context.enqueueWork(() -> processMessage(clientWorld.get(), message));
    }

    /**
     * Process the Message
     *
     * @param world Client World
     * @param message Message
     */
    private static void processMessage(ClientWorld world, DraggedItemClientMessage message) {
        assert Minecraft.getInstance().player != null;
        DraggedItemCapability capability = BundleCapabilities.getDraggedItemCapability(Minecraft.getInstance().player);
        if(capability != null) {
            capability.setDraggedItemStack(message.draggedItemStack);
        }
    }
}
