package com.bundles.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Bundle Resources
 *
 * @author JimiIT92
 */
public final class BundleResources {
    /**
     * Mod ID
     */
    public static final String MOD_ID = "bundles";
    /**
     * Bundle Item Resource Name
     */
    public static final String BUNDLE_ITEM_RESOURCE_NAME = "bundle";
    /**
     * Bundle Full NBT Tag Resource Location
     */
    public static final ResourceLocation BUNDLE_FULL_NBT_RESOURCE_LOCATION = new ResourceLocation(MOD_ID, "bundle_full");
    /**
     * Bundle Items NBT Tag Resource Location
     */
    public static final String BUNDLE_ITEMS_LIST_NBT_RESOURCE_LOCATION = "bundle_items";
    /**
     * Max Bundle Items Count
     */
    public static final int MAX_BUNDLE_ITEMS = 64;
    /**
     * Network Channel
     */
    public static SimpleChannel NETWORK;
    /**
     * Bundle Server Message ID
     */
    public static final byte BUNDLE_SERVER_MESSAGE_ID = 1;
    /**
     * Bundle Client Message ID
     */
    public static final byte BUNDLE_CLIENT_MESSAGE_ID = 2;
    /**
     * Message Protocol Version
     */
    public static final String MESSAGE_PROTOCOL_VERSION = "1.4";
    /**
     * Network Resource Location
     */
    public static final ResourceLocation NETWORK_RESOURCE_LOCATION = new ResourceLocation(MOD_ID, "network_channel");
    /**
     * Bundle Ignored Blocks Tag
     */
    public static final ResourceLocation BUNDLE_IGNORED_BLOCKS_TAG = new ResourceLocation(MOD_ID, "bundle_ignored_blocks");
    /**
     * Bundle Ignored Items Tag
     */
    public static final ResourceLocation BUNDLE_IGNORED_ITEMS_TAG = new ResourceLocation(MOD_ID, "bundle_ignored_items");
}
