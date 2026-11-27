package com.moonsworth.lunar.replaymod.v1_21_6.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moonsworth.lunar.client.external.replaymod.ReplayModLink;
import com.moonsworth.lunar.replaymod.v1_21_6.link.ReplayModGuiLink_Impl;
import com.moonsworth.lunar.replaymod.v1_21_6.link.ReplayModScuffedPacketWrapped;
import com.replaymod.core.versions.MCVer;
import com.replaymod.editor.gui.MarkerProcessor;
import com.replaymod.recording.packet.PacketListener;
import com.replaymod.replaystudio.PacketData;
import com.replaymod.replaystudio.protocol.Packet;
import io.netty.buffer.Unpooled;
import net.minecraft.network.ConnectionProtocol;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketListener.class)
public abstract class PacketListenerMixin_v1_20 {

    @Shadow public abstract ConnectionProtocol getConnectionState();

    @WrapOperation(
            method = "save(Lnet/minecraft/network/protocol/Packet;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/recording/packet/PacketListener;" +
                            "encodeMcPacket(Lnet/minecraft/network/ConnectionProtocol;" +
                            "Lnet/minecraft/network/protocol/Packet;)Lcom/replaymod/replaystudio/protocol/Packet;"
            )
    )
    private Packet ichor$lunar$scuffedReplayPacketWrapper(
            PacketListener instance, ConnectionProtocol connectionProtocol,
            net.minecraft.network.protocol.Packet packet, Operation<Packet> original) {
        try {
            if (packet instanceof ReplayModScuffedPacketWrapped wrappedPacket) {
                return getLunarPacket(wrappedPacket.getAssetPacket().toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return original.call(instance, connectionProtocol, packet);
    }

    @WrapOperation(
            method = "save(Lcom/replaymod/replaystudio/protocol/Packet;)V",
            at = @At(
                    value = "NEW",
                    target = "com/replaymod/replaystudio/PacketData"
            )
    )
    private PacketData ichor$lunar$scuffedPacketWrapper(long timestamp, Packet packet, Operation<PacketData> original) {
        try {
            if (packet.getId() == ReplayModLink.LUNAR_PACKET_ID) {
                return original.call(timestamp, getLunarPacket(packet.getBuf().array()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return original.call(timestamp, packet);
    }

    @Inject(
            method = "addMarker(Ljava/lang/String;)V",
            at = @At("HEAD")
    )
    private void ichor$addMarker(String name, CallbackInfo ci) {
        if (name != null && name.equals(MarkerProcessor.MARKER_NAME_END_CUT)) {
            ReplayModGuiLink_Impl.setRecordedThisSession(true);
        }
    }

    private Packet getLunarPacket(byte[] bytes) {
        return new Packet(
                MCVer.getPacketTypeRegistry(getConnectionState() == ConnectionProtocol.LOGIN),
                ReplayModLink.LUNAR_PACKET_ID,
                Unpooled.wrappedBuffer(bytes)
        );
    }

}
