package com.bundles.capability;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Dragged Item Capability Implementation
 *
 * @author JimiIT92
 */
public class DraggedItemCapability {

    /**
     * Dragged Item Stack
     */
    private ItemStack draggedItemStack;

    /**
     * Default Constructor
     */
    public DraggedItemCapability() {
        this(ItemStack.EMPTY);
    }

    /**
     * Constructor. Set the Dragged Item Stack
     *
     * @param stack Item Stack
     */
    public DraggedItemCapability(ItemStack stack) {
        this.draggedItemStack = stack;
    }

    /**
     * Get the Dragged Item Stack
     *
     * @return Dragged Item Stack
     */
    public ItemStack getDraggedItemStack() {
        return this.draggedItemStack;
    }

    /**
     * Set the Dragged Item Stack
     *
     * @param stack Dragged Item Stack
     */
    public void setDraggedItemStack(ItemStack stack) {
        this.draggedItemStack = stack;
    }

    /**
     * Dragged Item Capability NBT Storage
     */
    public static class DraggedItemCapabilityNBTStorage implements Capability.IStorage<DraggedItemCapability> {

        /**
         * Write Capability NBTs
         *
         * @param capability Capability class
         * @param instance Capability Instance
         * @param side Direction
         * @return Capability NBT
         */
        @Override
        public INBT writeNBT(Capability<DraggedItemCapability> capability, DraggedItemCapability instance, Direction side) {
            return ItemStackHelper.saveAllItems(new CompoundNBT(), NonNullList.withSize(1, instance.getDraggedItemStack()));
        }

        /**
         * Read Capability NBTs
         *
         * @param capability Capability class
         * @param instance Capability instance
         * @param side Direction
         * @param nbt Capability NBT
         */
        @Override
        public void readNBT(Capability<DraggedItemCapability> capability, DraggedItemCapability instance, Direction side, INBT nbt) {
            NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
            ItemStackHelper.loadAllItems((CompoundNBT)nbt, items);
            instance.setDraggedItemStack(items.get(0));
        }
    }
}
