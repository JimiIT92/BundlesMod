package com.bundles;

import com.bundles.capability.DraggedItemCapability;
import com.bundles.event.BundleEvents;
import com.bundles.init.BundleCapabilities;
import com.bundles.init.BundleItems;
import com.bundles.network.handler.ClientMessageHandler;
import com.bundles.network.handler.ServerMessageHandler;
import com.bundles.network.message.DraggedItemClientMessage;
import com.bundles.network.message.DraggedItemServerMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;

/**
 * Manage Inventory space with the new Bundles!
 */
@Mod(Bundles.MOD_ID)
public class Bundles {

    /**
     * Mod ID
     */
    public static final String MOD_ID = "bundles";
    /**
     * Network Channel
     */
    public static SimpleChannel NETWORK;
    /**
     * Dragged Item Capability Server Message ID
     */
    public static final byte DRAGGED_ITEM_SERVER_MESSAGE_ID = 1;
    /**
     * Dragged Item Capability Client Message ID
     */
    public static final byte DRAGGED_ITEM_CLIENT_MESSAGE_ID = 2;
    /**
     * Message Protocol Version
     */
    public static final String MESSAGE_PROTOCOL_VERSION = "1.0";
    /**
     * Network Resource Location
     */
    public static final ResourceLocation NETWORK_RESOURCE_LOCATION = new ResourceLocation(MOD_ID, "network_channel");

    /**
     * Constructor. Initialize the Mod
     */
    public Bundles() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onCommonSetup);
        BundleItems.ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Register Bundle Events
     *
     * @param event FML Common Setup Event
     */
    public void onCommonSetup(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(
                DraggedItemCapability.class,
                new DraggedItemCapability.DraggedItemCapabilityNBTStorage(),
                DraggedItemCapability::new
        );
        MinecraftForge.EVENT_BUS.register(BundleCapabilities.class);
        MinecraftForge.EVENT_BUS.register(BundleEvents.class);

        NETWORK = NetworkRegistry.newSimpleChannel(
                NETWORK_RESOURCE_LOCATION, () -> MESSAGE_PROTOCOL_VERSION,
                ClientMessageHandler::isThisProtocolAcceptedByClient,
                ServerMessageHandler::isThisProtocolAcceptedByServer);
        NETWORK.registerMessage(DRAGGED_ITEM_SERVER_MESSAGE_ID, DraggedItemServerMessage.class,
                DraggedItemServerMessage::encode, DraggedItemServerMessage::decode,
                ServerMessageHandler::onMessageReceived,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        NETWORK.registerMessage(DRAGGED_ITEM_CLIENT_MESSAGE_ID, DraggedItemClientMessage.class,
                DraggedItemClientMessage::encode, DraggedItemClientMessage::decode,
                ClientMessageHandler::onMessageReceived,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
