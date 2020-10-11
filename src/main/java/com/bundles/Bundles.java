package com.bundles;

import com.bundles.event.BundleEvents;
import com.bundles.init.BundleItems;
import com.bundles.init.BundleResources;
import com.bundles.network.handler.BundleClientMessageHandler;
import com.bundles.network.handler.BundleServerMessageHandler;
import com.bundles.network.message.BundleClientMessage;
import com.bundles.network.message.BundleServerMessage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;

import java.util.Optional;

/**
 * Manage Inventory space with the new Bundles!
 */
@Mod(BundleResources.MOD_ID)
public class Bundles {

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
        MinecraftForge.EVENT_BUS.register(BundleEvents.class);
        BundleResources.NETWORK = NetworkRegistry.newSimpleChannel(
                BundleResources.NETWORK_RESOURCE_LOCATION, () -> BundleResources.MESSAGE_PROTOCOL_VERSION,
                BundleClientMessageHandler::isThisProtocolAcceptedByClient,
                BundleServerMessageHandler::isThisProtocolAcceptedByServer);
        BundleResources.NETWORK.registerMessage(BundleResources.BUNDLE_SERVER_MESSAGE_ID, BundleServerMessage.class,
                BundleServerMessage::encode, BundleServerMessage::decode,
                BundleServerMessageHandler::onMessageReceived,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        BundleResources.NETWORK.registerMessage(BundleResources.BUNDLE_CLIENT_MESSAGE_ID, BundleClientMessage.class,
                BundleClientMessage::encode, BundleClientMessage::decode,
                BundleClientMessageHandler::onMessageReceived,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
