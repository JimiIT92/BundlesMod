package com.bundles.network;

import net.minecraft.network.PacketBuffer;

/**
 * Network Message for playing Bundle sounds to the Client
 */
public class BundleSoundMessage {

    /**
     * Sound ID
     */
    public int soundId;

    /**
     * Default Constructor. Sets an empty Sound ID
     */
    public BundleSoundMessage() {
        this(0);
    }

    /**
     * Constructor. Sets the Bundle Sound ID
     *
     * @param soundId Sound ID
     */
    public BundleSoundMessage(int soundId) {
        this.soundId = soundId;
    }

    /**
     * Deserialize the Message
     *
     * @param buffer Packet Buffer
     * @return Deserialized Message
     */
    public static BundleSoundMessage decode(PacketBuffer buffer) {
        BundleSoundMessage message = new BundleSoundMessage();
        message.soundId = buffer.readInt();
        return message;
    }

    /**
     * Serialize the Message
     *
     * @param buffer Packet Buffer
     */
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.soundId);
    }
}