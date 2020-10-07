package com.bundles.event;

import com.bundles.container.BundleCreativeContainer;
import com.bundles.container.BundlePlayerContainer;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Bundle Events
 *
 * @author JimiIT92
 */
public final class BundleEvents {

    /**
     * Set the Container on Player Login
     *
     * @param event Player Logged In event
     */
    @SubscribeEvent
    public static void onPlayerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
        Container container = event.getPlayer().openContainer;
        PlayerEntity player = event.getPlayer();
        if(container instanceof PlayerContainer) {
            event.getPlayer().openContainer = new BundlePlayerContainer(player, player.container.isLocalWorld);
        } else if(container instanceof CreativeScreen.CreativeContainer) {
            event.getPlayer().openContainer = new BundleCreativeContainer(player);
        }
    }
}
