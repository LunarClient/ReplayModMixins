package com.moonsworth.lunar.replaymod.v1_19.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.moonsworth.lunar.replaymod.v1_19.link.ReplayModGuiLink_Impl;
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
public abstract class GuiReplayViewerMixin_v1_19 {

    @Shadow
    public abstract GuiReplayViewer.GuiReplayList getThis();

    @ModifyExpressionValue(
            method = "lambda$new$3(Lcom/replaymod/lib/de/johni0702/minecraft/gui/utils/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lcom/replaymod/replaystudio/replay/ReplayFile;getMetaData()Lcom/replaymod/replaystudio/replay/ReplayMetaData;")
    )
    private ReplayMetaData ichor$onReplayLoad(ReplayMetaData metaData) {
        if (metaData.getDuration() == 0) {
            throw new IllegalStateException("[Lunar] Invalid replay detected, with zero length, skipping: " + metaData);
        }

        return metaData;
    }

    @Inject(
            method = "typeKey",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$typeKey(ReadablePoint mousePosition, int keyCode, char keyChar, boolean ctrlDown, boolean shiftDown, CallbackInfoReturnable<Boolean> ci) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            getThis().getMinecraft().setScreen(null);
            ci.setReturnValue(true);
            return;
        }

        // We have a save option open
        if (ReplayModGuiLink_Impl.savingReplayOpen > 0) {
            ci.setReturnValue(false);
        }
    }
}
