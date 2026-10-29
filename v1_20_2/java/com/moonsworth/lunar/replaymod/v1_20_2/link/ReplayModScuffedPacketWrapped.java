package com.moonsworth.lunar.replaymod.v1_20_2.link;

import com.google.protobuf.Any;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class ReplayModScuffedPacketWrapped implements Packet {

    private final Any assetPacket;

    public ReplayModScuffedPacketWrapped(Any assetPacket) {
        this.assetPacket = assetPacket;
    }



    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        // NO OP
    }

    @Override
    public void handle(PacketListener packetListener) {
        // NO OP
    }

    public Any getAssetPacket() {
        return assetPacket;
    }
}
