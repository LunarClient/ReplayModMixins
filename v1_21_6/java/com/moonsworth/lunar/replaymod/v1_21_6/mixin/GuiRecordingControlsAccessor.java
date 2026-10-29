package com.moonsworth.lunar.replaymod.v1_21_6.mixin;

import com.replaymod.recording.gui.GuiRecordingControls;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiRecordingControls.class)
public interface GuiRecordingControlsAccessor {
    @Invoker("updateState")
    void bridge$updateState();
}
