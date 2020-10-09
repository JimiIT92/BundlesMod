package com.bundles.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * @author JimiIT92
 */
public class BundleServerMessage {

    public ItemStack bundle;
    public int slotId;
    public boolean empty;

    public BundleServerMessage() {
        this(ItemStack.EMPTY,0, false);
    }

    public BundleServerMessage(ItemStack bundle, int slotId, boolean empty) {
        this.bundle = bundle;
        this.slotId = slotId;
        this.empty = empty;
    }

    public static BundleServerMessage decode(PacketBuffer buffer) {
        BundleServerMessage message = new BundleServerMessage();
        message.bundle = buffer.readItemStack();
        message.slotId = buffer.readInt();
        message.empty = buffer.readBoolean();
        return message;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeItemStack(this.bundle);
        buffer.writeInt(this.slotId);
        buffer.writeBoolean(this.empty);
    }
}
