package com.bundles.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * @author JimiIT92
 */
public class BundleClientMessage {

    public ItemStack bundle;
    public int slotId;
    public ItemStack slotStack;
    public boolean empty;

    public BundleClientMessage() {
        this(ItemStack.EMPTY,0, ItemStack.EMPTY, false);
    }

    public BundleClientMessage(ItemStack bundle, int slotId, ItemStack slotStack, boolean empty) {
        this.bundle = bundle;
        this.slotId = slotId;
        this.slotStack = slotStack;
        this.empty = empty;
    }

    public static BundleClientMessage decode(PacketBuffer buffer) {
        BundleClientMessage message = new BundleClientMessage();
        message.bundle = buffer.readItemStack();
        message.slotId = buffer.readInt();
        message.slotStack = buffer.readItemStack();
        message.empty = buffer.readBoolean();
        return message;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeItemStack(this.bundle);
        buffer.writeInt(this.slotId);
        buffer.writeItemStack(this.slotStack);
        buffer.writeBoolean(this.empty);
    }
}
