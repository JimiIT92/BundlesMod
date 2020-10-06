package com.bundles;

import com.bundles.init.BundleItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
     * Constructor. Initialize the Mod
     */
    public Bundles() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BundleItems.ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
