package com.moonsworth.lunar.replaymod.forge.v1_12.link;

import com.google.protobuf.Any;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

public class ReplayModScuffedPacketWrapped implements Packet {

    private Any assetPacket;

    public ReplayModScuffedPacketWrapped(Any assetPacket) {
        this.assetPacket = assetPacket;
    }

    public Any getAssetPacket() {
        return assetPacket;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {

    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {

    }

    @Override
    public void processPacket(INetHandler iNetHandler) {

    }
}
