package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.moonsworth.lunar.client.event.EventBus;
import com.moonsworth.lunar.client.event.impl.world.EventRenderTick;
import com.replaymod.render.hooks.EntityRendererHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRendererHandler.class)
public class EntityRendererHandlerMixin_v1_12 {

    @Inject(
            method = "renderWorld(FJ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;onRenderTickStart(F)V"
            )
    )
    private void ichor$renderWorld(float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        EventBus.getBus().post(new EventRenderTick.Pre(partialTicks));
    }

    @Inject(
            method = "renderWorld(FJ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;onRenderTickEnd(F)V"
            )
    )
    private void ichor$renderWorld$post(float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
        EventBus.getBus().post(
                EventRenderTick.Post.class,
                () -> new EventRenderTick.Post(partialTicks));
    }
}
