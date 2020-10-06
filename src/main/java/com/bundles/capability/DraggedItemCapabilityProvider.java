package com.bundles.capability;

import com.bundles.Bundles;
import com.bundles.init.BundleCapabilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Dragged Item Capability Provider
 *
 * @author JimiIT92
 */
public class DraggedItemCapabilityProvider implements ICapabilitySerializable<INBT> {

    /**
     * Dragged Item Capability
     */
    private DraggedItemCapability draggedItemCapability = new DraggedItemCapability();
    /**
     * Dragged Item Capability NBT
     */
    private final static String DRAGGED_ITEM_CAPABILITY_NBT = Bundles.MOD_ID + ":dragged_item_capability";

    /**
     * Get the Dragged Item Capability
     *
     * @param cap Capability
     * @param side Side
     * @param <T> Capability Type
     * @return Capability
     */
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == BundleCapabilities.DRAGGED_ITEM_CAPABILITY) {
            return (LazyOptional<T>)LazyOptional.of(() -> draggedItemCapability);
        }
        return LazyOptional.empty();
    }

    /**
     * Serialize the NBT Tag
     *
     * @return NBT Tag
     */
    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        INBT draggedItemNBT = BundleCapabilities.DRAGGED_ITEM_CAPABILITY.writeNBT(draggedItemCapability, null);
        assert draggedItemNBT != null;
        nbt.put(DRAGGED_ITEM_CAPABILITY_NBT, draggedItemNBT);
        return nbt;
    }

    /**
     * Deserialize the NBT Tag
     *
     * @param nbt NBT Tag
     */
    @Override
    public void deserializeNBT(INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT)nbt;
        INBT draggedItemNBT = compoundNBT.get(DRAGGED_ITEM_CAPABILITY_NBT);
        BundleCapabilities.DRAGGED_ITEM_CAPABILITY.readNBT(draggedItemCapability, null, draggedItemNBT);
    }
}
