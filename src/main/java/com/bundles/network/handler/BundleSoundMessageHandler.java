package com.bundles.network.handler;

import com.bundles.init.BundleResources;
import com.bundles.item.BundleItem;
import com.bundles.network.BundleSoundMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Server Message Handler for a Bundle Message
 */
public final class BundleSoundMessageHandler {

    /**
     * Check if the Protocol is accepted by the Client
     *
     * @param protocolVersion Protocol Version
     * @return True if the Protocol is accepted, False otherwise
     */
    public static boolean isThisProtocolAcceptedByClient(String protocolVersion) {
        return BundleResources.MESSAGE_PROTOCOL_VERSION.equals(protocolVersion);
    }

    /**
     * Handle the Bundle Message
     *
     * @param message Message
     * @param ctxSupplier Context Supplier
     */
    public static void onMessageReceived(final BundleSoundMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
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
     * Process the Bundle Message
     *
     * @param message Message
     * @param player Player
     */
    private static void processMessage(BundleSoundMessage message, ClientPlayerEntity player) {
        switch (message.soundId) {
            case 0:
            default:
                BundleItem.playInsertSoundFor(player);
                break;
            case 1:
                BundleItem.playRemoveOneSoundFor(player);
                break;
        }
    }
}