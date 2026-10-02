package com.moonsworth.lunar.replaymod.v1_19.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.replaymod.recording.gui.GuiRecordingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiRecordingOverlay.class)
public class GuiRecordingOverlayMixin_v1_19 {

    /**
     * @author Tre
     * @reason We do not want to render their overlay, Lunar will handle this for us.
     */
    @Overwrite
    public void renderRecordingIndicator(PoseStack stack) {
        // Do nothing.
    }

}
