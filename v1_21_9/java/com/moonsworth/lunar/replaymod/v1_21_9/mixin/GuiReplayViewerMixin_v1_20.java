package com.moonsworth.lunar.replaymod.v1_21_9.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.moonsworth.lunar.replaymod.v1_21_9.link.ReplayModGuiLink_Impl;
import com.replaymod.lib.de.johni0702.minecraft.gui.function.KeyInput;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadablePoint;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import com.replaymod.replaystudio.replay.ReplayMetaData;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiReplayViewer.GuiReplayList.class)
public abstract class GuiReplayViewerMixin_v1_20 {

    @Shadow
    public abstract GuiReplayViewer.GuiReplayList getThis();

    @ModifyExpressionValue(
            method = "lambda$new$3(Lcom/replaymod/lib/de/johni0702/minecraft/gui/utils/Consumer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/replaystudio/replay/ReplayFile;getMetaData()" +
                            "Lcom/replaymod/replaystudio/replay/ReplayMetaData;"
            )
    )
    private ReplayMetaData ichor$onReplayLoad(ReplayMetaData metaData) {
        if (metaData.getDuration() == 0) {
            throw new IllegalStateException("[Lunar] Invalid replay detected, with zero length, skipping: " + metaData);
        }

        return metaData;
    }

    @Inject(
            method = "handleKey",
            at = @At("HEAD"),
            cancellable = true
    )
    private void ichor$typeKey(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        if (input.key == GLFW.GLFW_KEY_ESCAPE) {
            getThis().getMinecraft().setScreen(null);
            cir.setReturnValue(true);
            return;
        }

        // We have a save option open
        if (ReplayModGuiLink_Impl.savingReplayOpen > 0) {
            cir.setReturnValue(false);
        }
    }
}
