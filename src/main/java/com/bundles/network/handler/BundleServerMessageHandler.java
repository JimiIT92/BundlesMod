package com.bundles.network.handler;

import com.bundles.init.BundleResources;
import com.bundles.network.message.BundleClientMessage;
import com.bundles.network.message.BundleServerMessage;
import com.bundles.util.BundleItemUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

/**
 * @author JimiIT92
 */
public class BundleServerMessageHandler {

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
     * Handle messages
     *
     * @param message Message
     * @param ctxSupplier Context Supplier
     */
    public static void onMessageReceived(final BundleServerMessage message, Supplier<NetworkEvent.Context> ctxSupplier) {
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
    private static void processMessage(BundleServerMessage message, ServerPlayerEntity playerEntity) {
        Container container = playerEntity.openContainer;
        Slot slot = container.getSlot(message.slotId);
        ItemStack slotStack = slot.getStack();
        boolean playEmptySound = false;
        if(message.empty) {
            playEmptySound = !BundleItemUtils.isEmpty(message.bundle);
            BundleItemUtils.emptyBundle(message.bundle, playerEntity);
            slotStack = message.bundle;
        } else {
            BundleItemUtils.addItemStackToBundle(message.bundle, slotStack);
            if(!playerEntity.isCreative() || !(container instanceof PlayerContainer)) {
                playerEntity.inventory.setItemStack(message.bundle);
            }
        }
        slot.putStack(slotStack);
        BundleResources.NETWORK.send(PacketDistributor.PLAYER.with(() -> playerEntity), new BundleClientMessage(message.bundle, message.slotId, slotStack, message.empty, playEmptySound));
    }
}
