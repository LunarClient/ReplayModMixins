package com.moonsworth.lunar.replaymod.v1_20_1.mixin;

import com.google.protobuf.Any;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.external.replaymod.ReplayModLink;
import com.replaymod.replay.FullReplaySender;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.nio.ByteBuffer;

@Mixin(FullReplaySender.class)
public abstract class FullReplaySenderMixin_v1_20 {

    @Inject(
            method = "deserializePacket",
            locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/ConnectionProtocol;createPacket(" +
                            "Lnet/minecraft/network/protocol/PacketFlow;ILnet/minecraft/network/FriendlyByteBuf;" +
                            ")Lnet/minecraft/network/protocol/Packet;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void ichor$deserializePacket(byte[] bytes, CallbackInfoReturnable<Packet> cir,
                                         ByteBuf  bb, FriendlyByteBuf pb, int id) {
        if (id == ReplayModLink.LUNAR_PACKET_ID) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(pb.readableBytes());
                pb.getBytes(pb.readerIndex(), buffer);
                buffer.flip();
                Any packet = Any.parseFrom(buffer);
                Lunar.getClient().getAssetsClient().processAny(packet);
                cir.setReturnValue(null);
            } catch (Exception ignored) {}
        }
    }
}
