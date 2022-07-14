package com.bundles.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Bundle Sounds
 *
 * @author JimiIT92
 */
public final class BundleSounds {

    /**
     * Bundle Sounds Registry
     */
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BundleResources.MOD_ID);

    /**
     * Bundle Drop Contents Sound
     */
    public static final RegistryObject<SoundEvent> BUNDLE_DROP_CONTENTS = register("bundle_drop_contents");
    /**
     * Bundle Insert Item Sound
     */
    public static final RegistryObject<SoundEvent> BUNDLE_INSERT = register("bundle_insert");
    /**
     * Bundle Remove One Item Sound
     */
    public static final RegistryObject<SoundEvent> BUNDLE_REMOVE_ONE = register("bundle_remove_one");

    /**
     * Register a Sound
     *
     * @param name Sound Name
     * @return Registered Sound
     */
    private static RegistryObject<SoundEvent> register(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(BundleResources.MOD_ID, name)));
    }
}