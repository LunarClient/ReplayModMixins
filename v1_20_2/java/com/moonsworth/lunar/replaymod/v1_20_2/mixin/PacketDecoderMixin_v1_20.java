package com.moonsworth.lunar.replaymod.v1_20_2.mixin;

import com.google.protobuf.Any;
import com.moonsworth.lunar.client.Lunar;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketDecoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.nio.ByteBuffer;
import java.util.List;

@Mixin(PacketDecoder.class)
public abstract class PacketDecoderMixin_v1_20 {

    @Inject(
            method = "decode",
            locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/network/ConnectionProtocol$CodecData.createPacket (ILnet/minecraft/network/FriendlyByteBuf;)Lnet/minecraft/network/protocol/Packet;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void ichor$deserializePacket(ChannelHandlerContext context, ByteBuf in, List<Object> out, CallbackInfo ci, int var4, Attribute var5, ConnectionProtocol.CodecData var6, FriendlyByteBuf pb, int id) {
        if (id == -2) { // Lunar protobuf packet id
            try {
                ByteBuffer buffer = ByteBuffer.allocate(pb.readableBytes());
                pb.getBytes(pb.readerIndex(), buffer);
                buffer.flip();
                Any packet = Any.parseFrom(buffer);
                Lunar.getClient().getAssetsClient().processAny(packet);
                // push the reader index forward by the size of the packet
                pb.readerIndex(pb.readerIndex() + packet.getSerializedSize());
            } catch (Exception ignored) {}
        }
    }
}
