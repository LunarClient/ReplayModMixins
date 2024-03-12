package com.moonsworth.lunar.replaymod.v1_20_2.mixin;

import com.moonsworth.lunar.client.event.EventBus;
import com.moonsworth.lunar.client.event.impl.EventState;
import com.moonsworth.lunar.client.event.impl.world.EventRenderTick;
import com.replaymod.render.hooks.EntityRendererHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRendererHandler.class)
public class EntityRendererHandlerMixin_v1_20 {

    @Inject(
            method = "renderWorld(FJ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/events/PreRenderCallback;preRender()V"
            )
    )
    public void ichor$renderWorld(float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        EventBus.getBus().post(new EventRenderTick(EventState.PRE, partialTicks));
    }

    @Inject(
            method = "renderWorld(FJ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/events/PostRenderCallback;postRender()V"
            )
    )
    public void ichor$renderWorld$post(float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        EventBus.getBus().post(new EventRenderTick(EventState.POST, partialTicks));
    }
}
