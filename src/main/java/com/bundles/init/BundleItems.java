package com.bundles.init;

import com.bundles.Bundles;
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
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bundles.MOD_ID);

    /**
     * Bundle
     */
    public static final RegistryObject<Item> BUNDLE = ITEMS.register("bundle", BundleItem::new);
}
