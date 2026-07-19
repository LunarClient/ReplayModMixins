package com.moonsworth.lunar.replaymod.v26_2.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.OpenGuiScreenCallback;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.PostRenderScreenCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin_v1_20 {

    @Shadow
    @Final
    public Minecraft minecraft;

    @Inject(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;extractRenderStateWithTooltipAndSubtitles(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void lunar$postRenderScreen(DeltaTracker tracker, boolean renderWorld, boolean loadingFinished, CallbackInfo ci) {
        PostRenderScreenCallback.EVENT.invoker()
                .postRenderScreen(new GuiGraphicsExtractor(this.minecraft, this.minecraft.gameRenderer.gameRenderState.guiRenderState, 0, 0), // TODO mouse coords?
                        tracker.getRealtimeDeltaTicks());
    }

    @Inject(
            method = "setScreen",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/Gui;screen:Lnet/minecraft/client/gui/screens/Screen;"
            )
    )
    private void openGuiScreen(Screen newGuiScreen, CallbackInfo ci) {
        OpenGuiScreenCallback.EVENT.invoker().openGuiScreen(newGuiScreen);
    }
}
