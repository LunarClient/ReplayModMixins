package com.moonsworth.lunar.replaymod.v1_21_0.link;

import com.google.protobuf.Any;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.resources.ResourceLocation;

public class ReplayModScuffedPacketWrapped implements Packet {

    private final Any assetPacket;
    private final PacketType type;

    public ReplayModScuffedPacketWrapped(Any assetPacket) {
        this.assetPacket = assetPacket;
        this.type = new PacketType(PacketFlow.CLIENTBOUND, ResourceLocation.fromNamespaceAndPath("lunar", "scuffedreplaymodpacket"));
    }

    @Override
    public PacketType<? extends Packet> type() {
        return type;
    }

    @Override
    public void handle(PacketListener packetListener) {
        // NO OP
    }

    public Any getAssetPacket() {
        return assetPacket;
    }
}
