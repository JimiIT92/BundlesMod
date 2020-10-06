package com.bundles.util;

import com.bundles.Bundles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Register Client Events
 *
 * @author JimiIT92
 */
@Mod.EventBusSubscriber(modid = Bundles.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEventBusSubscriber {

    /**
     * Register Client Events
     *
     * @param event FML Client Setup Event
     */
    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        /*BundleItems.BUNDLE.get().addPropertyOverride(new ResourceLocation(TutorialMod.MOD_ID, "count"), new IItemPropertyGetter() {
            @Override
            public float call(ItemStack itemStack, @Nullable World worldIn, @Nullable LivingEntity entityIn) {
                switch (itemStack.getCount()) {
                    case 1:
                        return 0.25F;
                    case 2:
                        return 0.5F;
                    case 3:
                        return 0.75F;
                    case 4:
                        return 1.0F;
                    default:
                        return 0.0F;
                }
            }
        });*/
    }
}
