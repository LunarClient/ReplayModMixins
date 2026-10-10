package com.moonsworth.lunar.replaymod.v1_19.mixin;

import com.moonsworth.fabric.versioned.ModAvailable;
import com.moonsworth.lunar.replaymod.v1_19.link.ReplayModGuiLink_Impl;
import com.moonsworth.lunar.replaymod.v1_19.link.ReplayModScuffedPacketWrapped;
import com.replaymod.core.versions.MCVer;
import com.replaymod.editor.gui.MarkerProcessor;
import com.replaymod.recording.packet.PacketListener;
import com.replaymod.replaystudio.PacketData;
import com.replaymod.replaystudio.protocol.Packet;
import io.netty.buffer.Unpooled;
import net.minecraft.network.ConnectionProtocol;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketListener.class)
public abstract class PacketListenerMixin_v1_19 {

    @Shadow public abstract ConnectionProtocol getConnectionState();

    @ModAvailable(value = "replaymod", version = "<1.19.4-2.6.16")
    @Shadow
    public static Packet encodeMcPacket(ConnectionProtocol connectionProtocol, net.minecraft.network.protocol.Packet packet) throws Exception {
        return null;
    }

    @ModAvailable(value = "replaymod", version = ">=1.19.4-2.6.16")
    @Dynamic
    @Shadow(aliases = "encodeMcPacket")
    public Packet notStaticEncodeMcPacket(ConnectionProtocol connectionProtocol, net.minecraft.network.protocol.Packet packet) throws Exception {
        return null;
    }

    @ModAvailable(value = "replaymod", version = "<1.19.4-2.6.16")
    @Redirect(
            method = "save(Lnet/minecraft/network/protocol/Packet;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/recording/packet/PacketListener;" +
                            "encodeMcPacket(Lnet/minecraft/network/ConnectionProtocol;" +
                            "Lnet/minecraft/network/protocol/Packet;)Lcom/replaymod/replaystudio/protocol/Packet;"
            )
    )
    private Packet ichor$lunar$scuffedReplayPacketWrapper(ConnectionProtocol connectionProtocol, net.minecraft.network.protocol.Packet packet) throws Exception {
        try {
            if (packet instanceof ReplayModScuffedPacketWrapped wrappedPacket) {
                return getLunarPacket(wrappedPacket.getAssetPacket().toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeMcPacket(connectionProtocol, packet);
    }

    @ModAvailable(value = "replaymod", version = ">=1.19.4-2.6.16")
    @Dynamic
    @Redirect(
            method = "save(Lnet/minecraft/network/protocol/Packet;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/recording/packet/PacketListener;" +
                            "encodeMcPacket(Lnet/minecraft/network/ConnectionProtocol;" +
                            "Lnet/minecraft/network/protocol/Packet;)Lcom/replaymod/replaystudio/protocol/Packet;"
            )
    )
    private Packet ichor$lunar$scuffedReplayPacketWrapperNotStatic(PacketListener packetListener, ConnectionProtocol connectionProtocol, net.minecraft.network.protocol.Packet packet) throws Exception {
        try {
            if (packet instanceof ReplayModScuffedPacketWrapped wrappedPacket) {
                return getLunarPacket(wrappedPacket.getAssetPacket().toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.notStaticEncodeMcPacket(connectionProtocol, packet);
    }

    @Redirect(
            method = "save(Lcom/replaymod/replaystudio/protocol/Packet;)V",
            at = @At(
                    value = "NEW",
                    target = "com/replaymod/replaystudio/PacketData"
            )
    )
    private PacketData ichor$lunar$scuffedPacketWrapper(long timestamp, Packet packet) {
        try {
            if (packet.getId() == -2) {
                return new PacketData(timestamp, getLunarPacket(packet.getBuf().array()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PacketData(timestamp, packet);
    }

    @Inject(
            method = "addMarker(Ljava/lang/String;)V",
            at = @At("HEAD")
    )
    private void ichor$addMarker(String name, CallbackInfo ci) {
        if (name != null && name.equals(MarkerProcessor.MARKER_NAME_END_CUT)) {
            ReplayModGuiLink_Impl.recordedThisSession = true;
        }
    }

    private Packet getLunarPacket(byte[] bytes) {
        return new Packet(
                MCVer.getPacketTypeRegistry(getConnectionState() == ConnectionProtocol.LOGIN),
                -2, // Lunar protobuf packet id
                Unpooled.wrappedBuffer(bytes)
        );
    }

}
