package com.moonsworth.lunar.replaymod.v26_1.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.PostRenderScreenCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import org.spongepowered.asm.mixin.Final;
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
    @Final
    public GameRenderState gameRenderState;

    @Inject(
            method = "extractGui",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderStateWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void lunar$postRenderScreen(DeltaTracker tracker, boolean renderWorld, boolean loadingFinished, CallbackInfo ci) {
        PostRenderScreenCallback.EVENT.invoker()
                .postRenderScreen(new GuiGraphicsExtractor(this.getMinecraft(), this.gameRenderState.guiRenderState, 0, 0), // TODO mouse coords?
                        tracker.getRealtimeDeltaTicks());
    }
}
