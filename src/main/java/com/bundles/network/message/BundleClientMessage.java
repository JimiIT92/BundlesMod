package com.bundles.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * Bundle Client Message
 *
 * @author JimiIT92
 */
public class BundleClientMessage {

    /**
     * Bundle Item Stack
     */
    public ItemStack bundle;
    /**
     * Item Stack Slot Id
     */
    public int slotId;
    /**
     * Item Stack for Bundle
     */
    public ItemStack slotStack;
    /**
     * If the Bundle should be cleared
     */
    public boolean empty;

    /**
     * Default constructor
     */
    public BundleClientMessage() {
        this(ItemStack.EMPTY,0, ItemStack.EMPTY, false);
    }

    /**
     * Constructor
     *
     * @param bundle Bundle Item Stack
     * @param slotId Item Stack Slot Id
     * @param slotStack Item Stack for Bundle
     * @param empty If the Bundle should be cleared
     */
    public BundleClientMessage(ItemStack bundle, int slotId, ItemStack slotStack, boolean empty) {
        this.bundle = bundle;
        this.slotId = slotId;
        this.slotStack = slotStack;
        this.empty = empty;
    }

    /**
     * Deserialize the Message
     *
     * @param buffer Packet Buffer
     * @return Message
     */
    public static BundleClientMessage decode(PacketBuffer buffer) {
        BundleClientMessage message = new BundleClientMessage();
        message.bundle = buffer.readItemStack();
        message.slotId = buffer.readInt();
        message.slotStack = buffer.readItemStack();
        message.empty = buffer.readBoolean();
        return message;
    }

    /**
     * Serialize the Message
     *
     * @param buffer Packet Buffer
     */
    public void encode(PacketBuffer buffer) {
        buffer.writeItemStack(this.bundle);
        buffer.writeInt(this.slotId);
        buffer.writeItemStack(this.slotStack);
        buffer.writeBoolean(this.empty);
    }
}
