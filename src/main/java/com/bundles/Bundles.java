package com.bundles;

import com.bundles.init.BundleItems;
import com.bundles.init.BundleResources;
import com.bundles.init.BundleSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
        BundleItems.ITEMS.register(modEventBus);
        BundleSounds.SOUNDS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}