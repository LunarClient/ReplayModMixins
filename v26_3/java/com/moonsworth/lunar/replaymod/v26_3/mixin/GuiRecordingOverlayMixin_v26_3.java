package com.moonsworth.lunar.replaymod.v26_3.mixin;

import com.replaymod.recording.gui.GuiRecordingOverlay;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GuiRecordingOverlay.class)
public class GuiRecordingOverlayMixin_v26_3 {

    /**
     * @author Tre
     * @reason We do not want to render their overlay, Lunar will handle this for us.
     */
    @Overwrite
    public void renderRecordingIndicator(GuiGraphicsExtractor stack) {
        // Do nothing.
    }

}
