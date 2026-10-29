package com.moonsworth.lunar.replaymod.v1_21_11.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.moonsworth.lunar.client.event.EventBus;
import com.moonsworth.lunar.client.event.impl.world.EventRenderTick;
import com.replaymod.render.hooks.EntityRendererHandler;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRendererHandler.class)
public class EntityRendererHandlerMixin_v1_20 {

    @Dynamic
    @Inject(
            method = {
                    "renderWorld(FJ)V",
                    "renderWorld(FIIJ)V" // ReplayMod 2.6.28+
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/events/PreRenderCallback;preRender()V"
            )
    )
    private void ichor$renderWorld(CallbackInfo callbackInfo, @Local(argsOnly = true) float partialTicks) {
        EventBus.getBus().post(EventRenderTick.Pre.class, () -> new EventRenderTick.Pre(partialTicks));
    }

    @Dynamic
    @Inject(
            method = {
                    "renderWorld(FJ)V",
                    "renderWorld(FIIJ)V" // ReplayMod 2.6.28+
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/events/PostRenderCallback;postRender()V"
            )
    )
    private void ichor$renderWorld$post(CallbackInfo callbackInfo, @Local(argsOnly = true) float partialTicks) {
        EventBus.getBus().post(EventRenderTick.Post.class, () -> new EventRenderTick.Post(partialTicks));
    }
}
