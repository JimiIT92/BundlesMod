package com.bundles.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

/**
 * @author JimiIT92
 */
public class BundleClientMessage {

    public ItemStack bundle;
    public int slotId;
    public boolean empty;

    public BundleClientMessage() {
        this(ItemStack.EMPTY,0, false);
    }

    public BundleClientMessage(ItemStack bundle, int slotId, boolean empty) {
        this.bundle = bundle;
        this.slotId = slotId;
        this.empty = empty;
    }

    public static BundleClientMessage decode(PacketBuffer buffer) {
        BundleClientMessage message = new BundleClientMessage();
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
