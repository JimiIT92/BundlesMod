package com.bundles.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * Network Message for syncing Bundle contents between client and server
 */
public class BundleMessage {

    /**
     * Bundle Item Stack
     */
    public ItemStack bundle;
    /**
     * Item Stack to fit inside the Bundle
     */
    public ItemStack item;
    /**
     * Inventory Slot ID
     */
    public int slotId;
    /**
     * Clicked button
     */
    public int button;
    /**
     * If the Bundle is being stacked on other Items
     */
    public boolean stackOnOther;

    /**
     * Default Constructor. Sets an empty Bundle Item Stack
     */
    public BundleMessage() {
        this(ItemStack.EMPTY, ItemStack.EMPTY, 0, 0, false);
    }

    /**
     * Constructor. Sets the Bundle Item Stack
     *
     * @param bundle Bundle Item Stack
     * @param item Item Stack to fit inside the Bundle
     * @param slotId Inventory Slot ID
     * @param button Clicked button
     * @param stackOnOther If the Bundle is being stacked on other Items
     */
    public BundleMessage(ItemStack bundle, ItemStack item, int slotId, int button, boolean stackOnOther) {
        this.bundle = bundle;
        this.item = item;
        this.slotId = slotId;
        this.button = button;
        this.stackOnOther = stackOnOther;
    }

    /**
     * Deserialize the Message
     *
     * @param buffer Packet Buffer
     * @return Deserialized Message
     */
    public static BundleMessage decode(PacketBuffer buffer) {
        BundleMessage message = new BundleMessage();
        message.bundle = buffer.readItem();
        message.item = buffer.readItem();
        message.slotId = buffer.readInt();
        message.button = buffer.readInt();
        message.stackOnOther = buffer.readBoolean();
        return message;
    }

    /**
     * Serialize the Message
     *
     * @param buffer Packet Buffer
     */
    public void encode(PacketBuffer buffer) {
        buffer.writeItem(this.bundle);
        buffer.writeItem(this.item);
        buffer.writeInt(this.slotId);
        buffer.writeInt(this.button);
        buffer.writeBoolean(this.stackOnOther);
    }
}