package com.moonsworth.lunar.replaymod.v1_12.mixin;

import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.replaymod.v1_12.link.ReplayModGuiLink_v1_12;
import com.moonsworth.lunar.replaymod.v1_12.link.GuiRecordingControlsBridge;
import com.replaymod.recording.gui.GuiRecordingControls;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(GuiRecordingControls.class)
public abstract class GuiRecordingControlsMixin_v1_12 implements GuiRecordingControlsBridge {

    @Shadow private boolean paused;

    @Shadow private boolean stopped;

    @Shadow protected abstract void updateState();

    @Inject(
            method = "updateState",
            at = @At("HEAD")
    )
    public void ichor$updateState(CallbackInfo ci) {
        ReplayModGuiLink_v1_12.recordingPaused = paused;
        ReplayModGuiLink_v1_12.recordingStopped = stopped;
    }

    @Inject(
            method = "injectIntoIngameMenu",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$injectIntoIngameMenu(GuiScreen guiScreen, Collection<GuiButton> buttonList, CallbackInfo ci) {
        if (guiScreen instanceof GuiIngameMenu) {
            if (!Ref.client().getMods().getReplayMod().isEnabled()) {
                ci.cancel();
            }
        }
    }

    @Override
    public void bridge$setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    @Override
    public void bridge$setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void bridge$updateState() {
        updateState();
    }
}
