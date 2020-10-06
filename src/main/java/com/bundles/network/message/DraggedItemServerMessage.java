package com.bundles.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * Dragged Item Client Message
 *
 * @author JimiIT92
 */
public class DraggedItemServerMessage {

    /**
     * Dragged Item Stack
     */
    public ItemStack draggedItemStack;

    /**
     * Default Constructor.
     */
    public DraggedItemServerMessage() {
        this(ItemStack.EMPTY);
    }

    /**
     * Constructor. Set the Dragged Item Stack
     *
     * @param stack Dragged Item Stack
     */
    public DraggedItemServerMessage(ItemStack stack) {
        this.draggedItemStack = stack;
    }

    /**
     * Decode the Message
     *
     * @param buffer Packet Buffer
     * @return Message
     */
    public static DraggedItemServerMessage decode(PacketBuffer buffer) {
        DraggedItemServerMessage message = new DraggedItemServerMessage();
        message.draggedItemStack = buffer.readItemStack();
        return message;
    }

    /**
     * Encode the Message
     *
     * @param buffer Packet Buffer
     */
    public void encode(PacketBuffer buffer) {
        buffer.writeItemStack(this.draggedItemStack);
    }
}
