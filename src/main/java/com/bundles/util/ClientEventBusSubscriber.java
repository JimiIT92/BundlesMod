package com.bundles.util;

import com.bundles.event.BundleEvents;
import com.bundles.init.BundleItems;
import com.bundles.init.BundleResources;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Register Client Events
 *
 * @author JimiIT92
 */
@Mod.EventBusSubscriber(modid = BundleResources.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEventBusSubscriber {

    /**
     * Register Client Events
     *
     * @param event FML Client Setup Event
     */
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(BundleEvents.class);
        event.enqueueWork(() -> ItemModelsProperties.func_239418_a_(
                BundleItems.BUNDLE.get()
                , BundleResources.BUNDLE_FULL_NBT_RESOURCE_LOCATION
                , new IItemPropertyGetter() {
            @ParametersAreNonnullByDefault
            @Override
            public float call(ItemStack itemStack, @Nullable ClientWorld worldIn, @Nullable LivingEntity entityIn) {
                return BundleItemUtils.isFull(itemStack) ? 1.0F : 0.0F;
            }
        }));
    }
}
