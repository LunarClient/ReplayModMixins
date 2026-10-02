package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.moonsworth.lunar.replaymod.forge.v1_12.link.ReplayModGuiLink_v1_12;
import com.replaymod.lib.de.johni0702.minecraft.gui.function.KeyInput;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiReplayViewer.GuiReplayList.class)
public abstract class GuiReplayViewerMixin_v1_12 {

    @Shadow protected abstract GuiReplayViewer.GuiReplayList getThis();

    @Inject(
            method = "handleKey",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$typeKey(KeyInput keyInput, CallbackInfoReturnable<Boolean> ci) {
        if (keyInput.key == 0x01) {
            getThis().getMinecraft().displayGuiScreen(null);
            ci.setReturnValue(true);
            return;
        }

        // We have a save option open
        if (ReplayModGuiLink_v1_12.savingReplayOpen > 0) {
            ci.setReturnValue(false);
        }
    }
}
