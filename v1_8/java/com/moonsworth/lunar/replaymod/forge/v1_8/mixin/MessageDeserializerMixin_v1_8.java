package com.moonsworth.lunar.replaymod.forge.v1_8.mixin;

import com.google.protobuf.Any;
import com.moonsworth.lunar.client.Lunar;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.MessageDeserializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.nio.ByteBuffer;
import java.util.List;

@Mixin(MessageDeserializer.class)
public abstract class MessageDeserializerMixin_v1_8 {

    @Inject(
            method = "decode",
            locals = LocalCapture.CAPTURE_FAILHARD,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/EnumConnectionState;getPacket(" +
                            "Lnet/minecraft/network/EnumPacketDirection;I)Lnet/minecraft/network/Packet;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void ichor$deserializePacket(ChannelHandlerContext context, ByteBuf in, List<Object> out, CallbackInfo ci,
                                         PacketBuffer pb, int id) {
        if (id == -2) { // Lunar protobuf packet id
            try {
                ByteBuffer buffer = ByteBuffer.allocate(pb.readableBytes());
                pb.getBytes(pb.readerIndex(), buffer);
                buffer.flip();
                Any packet = Any.parseFrom(buffer);
                Lunar.getClient().getAssetsClient().processAny(packet);
                ci.cancel();
            } catch (Exception ignored) {}
        }
    }
}
