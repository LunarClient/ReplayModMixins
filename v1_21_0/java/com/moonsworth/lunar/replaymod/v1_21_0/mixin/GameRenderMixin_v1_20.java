package com.moonsworth.lunar.replaymod.v1_21_0.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.PostRenderScreenCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRenderMixin_v1_20 {

    @Shadow
    public abstract Minecraft getMinecraft();

    @Shadow
    public RenderBuffers renderBuffers;

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltip(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void postRenderScreen(DeltaTracker tracker, boolean renderWorld, CallbackInfo ci) {
        PostRenderScreenCallback.EVENT.invoker().postRenderScreen(new GuiGraphics(this.getMinecraft(), this.renderBuffers.bufferSource()), tracker.getRealtimeDeltaTicks());
    }
}
