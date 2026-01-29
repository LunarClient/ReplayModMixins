package com.moonsworth.lunar.replaymod.v1_21_0.mixin;

import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.replaymod.v1_21_0.link.ReplayModGuiLink_Impl;
import com.replaymod.recording.gui.GuiRecordingControls;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(GuiRecordingControls.class)
public class GuiRecordingControlsMixin_v1_20 {

    @Shadow public boolean paused;

    @Shadow public boolean stopped;

    @Inject(
            method = "updateState",
            at = @At("HEAD")
    )
    public void ichor$updateState(CallbackInfo ci) {
        ReplayModGuiLink_Impl.recordingPaused = paused;
        ReplayModGuiLink_Impl.recordingStopped = stopped;
    }

    @Inject(
            method = "injectIntoIngameMenu",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$injectIntoIngameMenu(Screen guiScreen, Collection<AbstractWidget> buttonList, CallbackInfo ci) {
        if (guiScreen instanceof PauseScreen) {
            if (!Ref.client().getMods().getReplayMod().isEnabled()) {
                ci.cancel();
            }
        }
    }
}
