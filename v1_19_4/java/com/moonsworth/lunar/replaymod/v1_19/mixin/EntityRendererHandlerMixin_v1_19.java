package com.moonsworth.lunar.replaymod.v1_19.mixin;

import com.moonsworth.lunar.client.event.EventBus;
import com.moonsworth.lunar.client.event.impl.world.EventRenderTick;
import com.replaymod.render.hooks.EntityRendererHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRendererHandler.class)
public class EntityRendererHandlerMixin_v1_19 {

    @Inject(
            method = "renderWorld(FJ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/events/PreRenderCallback;preRender()V"
            )
    )
    private void ichor$renderWorld(float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        EventBus.getBus().post(EventRenderTick.Pre.class, () -> new EventRenderTick.Pre(partialTicks));
    }

    @Inject(
            method = "renderWorld(FJ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/events/PostRenderCallback;postRender()V"
            )
    )
    private void ichor$renderWorld$post(float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        EventBus.getBus().post(EventRenderTick.Post.class, () -> new EventRenderTick.Post(partialTicks));
    }
}
