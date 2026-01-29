package com.moonsworth.lunar.replaymod.v1_21_0.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.PacketListener;
import net.minecraft.network.ProtocolSwapHandler;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PacketDecoder.class)
public abstract class PacketDecoderMixin_v1_21<T extends PacketListener> extends ByteToMessageDecoder implements ProtocolSwapHandler {

    @Inject(
            method = "decode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/Packet;type()Lnet/minecraft/network/protocol/PacketType;"
            ),
            cancellable = true
    )
    private void lunar$ignoreHandledLunarPackets(ChannelHandlerContext ctx, ByteBuf buf, List<Object> objs, CallbackInfo ci, @Local Packet<? extends T> packet) {
        // see IdDispatchCodecMixin, the returned Packet will be null if it's a lunar packet
        if (packet == null) {
            ci.cancel();
        }
    }

}
