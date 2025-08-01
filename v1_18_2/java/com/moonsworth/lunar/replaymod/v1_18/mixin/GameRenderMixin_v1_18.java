package com.moonsworth.lunar.replaymod.v1_18.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.PostRenderScreenCallback;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRenderMixin_v1_18 {

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void postRenderScreen(float partialTicks, long nanoTime, boolean renderWorld, CallbackInfo ci) {
        PostRenderScreenCallback.EVENT.invoker().postRenderScreen(new PoseStack(), partialTicks);
    }
}
