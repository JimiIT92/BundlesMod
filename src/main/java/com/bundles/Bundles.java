package com.bundles;

import com.bundles.init.BundleItems;
import com.bundles.init.BundleResources;
import com.bundles.init.BundleSounds;
import com.bundles.item.BundleItem;
import net.minecraft.item.ItemModelsProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
        modEventBus.addListener(this::onClientSetup);
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
}