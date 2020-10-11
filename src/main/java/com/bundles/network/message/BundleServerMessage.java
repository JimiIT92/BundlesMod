package com.bundles.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * Bundle Server Message
 *
 * @author JimiIT92
 */
public class BundleServerMessage {

    /**
     * Bundle Item Stack
     */
    public ItemStack bundle;
    /**
     * Item Stack Slot Id
     */
    public int slotId;
    /**
     * If the Bundle should be cleared
     */
    public boolean empty;

    /**
     * Default constructor
     */
    public BundleServerMessage() {
        this(ItemStack.EMPTY,0, false);
    }


    /**
     * Constructor
     *
     * @param bundle Bundle Item Stack
     * @param slotId Item Stack Slot Id
     * @param empty If the Bundle should be cleared
     */
    public BundleServerMessage(ItemStack bundle, int slotId, boolean empty) {
        this.bundle = bundle;
        this.slotId = slotId;
        this.empty = empty;
    }

    /**
     * Deserialize the Message
     *
     * @param buffer Packet Buffer
     * @return Message
     */
    public static BundleServerMessage decode(PacketBuffer buffer) {
        BundleServerMessage message = new BundleServerMessage();
        message.bundle = buffer.readItemStack();
        message.slotId = buffer.readInt();
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
        buffer.writeBoolean(this.empty);
    }
}
