package com.bundles.init;

import com.bundles.Bundles;
import com.bundles.capability.DraggedItemCapability;
import com.bundles.capability.DraggedItemCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


/**
 * Bundle Capabilities
 *
 * @author JimiIT92
 */
public class BundleCapabilities {

    /**
     * Dragged Item Capability
     */
    @CapabilityInject(DraggedItemCapability.class)
    public static Capability<DraggedItemCapability> DRAGGED_ITEM_CAPABILITY = null;

    /**
     * Attach Dragged Item Capability to Players
     *
     * @param event Attach Capabilities Event
     */
    @SubscribeEvent
    public static void onAttachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if(!event.isCanceled() && entity instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(Bundles.MOD_ID), new DraggedItemCapabilityProvider());
        }
    }

    /**
     * Get the Dragged Item Capability
     *
     * @param entity Entity
     * @return Dragged Item Capability
     */
    public static DraggedItemCapability getDraggedItemCapability(Entity entity) {
        return entity.getCapability(BundleCapabilities.DRAGGED_ITEM_CAPABILITY).resolve().orElse(null);
    }
}
