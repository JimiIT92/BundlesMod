package com.bundles.init;

import com.bundles.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Bundle Items
 *
 * @author JimiIT92
 */
public final class BundleItems {

    /**
     * Bundle Items Registry
     */
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, BundleResources.MOD_ID);

    /**
     * Bundle Item
     */
    public static final RegistryObject<Item> BUNDLE = ITEMS.register(BundleResources.BUNDLE_ITEM_RESOURCE_NAME, BundleItem::new);
}
