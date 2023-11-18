package com.moonsworth.lunar.replaymod.v1_8.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.versions.MatrixStack;
import com.replaymod.recording.gui.GuiRecordingOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiRecordingOverlay.class)
public class GuiRecordingOverlayMixin_v1_8 {

    /**
     * @author Tre
     * @reason We do not want to render their overlay, Lunar will handle this for us.
     */
    @Overwrite
    public void renderRecordingIndicator(MatrixStack stack) {
        // Do nothing.
    }

}
