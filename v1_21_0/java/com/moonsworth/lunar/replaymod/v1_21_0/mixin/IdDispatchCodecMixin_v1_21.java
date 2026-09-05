package com.moonsworth.lunar.replaymod.v1_21_0.mixin;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.llamalad7.mixinextras.sugar.Local;
import com.moonsworth.lunar.client.Lunar;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.IdDispatchCodec;
import net.minecraft.network.codec.StreamCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.ByteBuffer;

@Mixin(IdDispatchCodec.class)
public abstract class IdDispatchCodecMixin_v1_21<B extends ByteBuf, V, T> implements StreamCodec<B, V> {

    @Inject(
            method = "decode(Lio/netty/buffer/ByteBuf;)Ljava/lang/Object;",
            at = @At(value = "NEW", target = "Lio/netty/handler/codec/DecoderException;", ordinal = 0),
            cancellable = true
    )
    private void lunar$decodeProtobufPackets(B buffer, CallbackInfoReturnable<V> cir, @Local int id) throws InvalidProtocolBufferException {
        if (id == -2) {
            // handle Lunar packet
            int capacity = buffer.readableBytes();
            ByteBuffer lunarPacketBuffer = ByteBuffer.allocate(capacity);
            buffer.getBytes(buffer.readerIndex(), lunarPacketBuffer);
            lunarPacketBuffer.flip();
            Any packet = Any.parseFrom(lunarPacketBuffer);
            Lunar.getClient().getAssetsClient().processAny(packet);
            // push the reader index forward by the size of the packet
            buffer.readerIndex(buffer.readerIndex() + packet.getSerializedSize());
            cir.setReturnValue(null);
        }
    }

}
