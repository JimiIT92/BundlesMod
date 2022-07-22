package com.bundles;

import com.bundles.init.BundleItems;
import com.bundles.init.BundleResources;
import com.bundles.init.BundleSounds;
import com.bundles.item.BundleItem;
import com.bundles.network.BundleMessage;
import com.bundles.network.BundleSoundMessage;
import com.bundles.network.handler.BundleServerMessageHandler;
import com.bundles.network.handler.BundleSoundMessageHandler;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onCommonSetup);
        BundleItems.ITEMS.register(modEventBus);
        BundleSounds.SOUNDS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Setup the Item model override for a filled Bundle
     *
     * @param event FML Client Setup Event
     */
    public void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemModelsProperties.register(BundleItems.BUNDLE.get(),
                                                                BundleResources.BUNDLE_FULL_NBT_RESOURCE_LOCATION,
                                                                (bundle, world, entity) -> ((BundleItem)bundle.getItem()).hasContent(bundle) ? 1 : 0));
    }

    /**
     * Setup the Bundle Network Messages
     *
     * @param event FML Common Setup Event
     */
    public void onCommonSetup(final FMLCommonSetupEvent event) {
        BundleResources.NETWORK = NetworkRegistry.newSimpleChannel(
                BundleResources.NETWORK_RESOURCE_LOCATION, () -> BundleResources.MESSAGE_PROTOCOL_VERSION,
                BundleSoundMessageHandler::isThisProtocolAcceptedByClient,
                BundleServerMessageHandler::isThisProtocolAcceptedByServer);
        BundleResources.NETWORK.registerMessage(BundleResources.BUNDLE_SERVER_MESSAGE_ID, BundleMessage.class,
                BundleMessage::encode, BundleMessage::decode,
                BundleServerMessageHandler::onMessageReceived,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        BundleResources.NETWORK.registerMessage(BundleResources.BUNDLE_SOUND_MESSAGE_ID, BundleSoundMessage.class,
                BundleSoundMessage::encode, BundleSoundMessage::decode,
                BundleSoundMessageHandler::onMessageReceived,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}