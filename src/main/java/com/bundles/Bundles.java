package com.bundles;

import com.bundles.event.BundleEvents;
import com.bundles.init.BundleItems;
import com.bundles.init.BundleResources;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
    }
}
