package com.moonsworth.lunar.replaymod.v1_8.mixin;

import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.replaymod.v1_8.link.ReplayModGuiLink_v1_8;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableColor;
import com.replaymod.recording.gui.GuiSavingReplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSavingReplay.class)
public class GuiSavingReplayMixin_v1_8 {

    @Redirect(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/utils/Colors;BLACK:Lcom/replaymod/lib/de/johni0702/minecraft/gui/utils/lwjgl/ReadableColor;"
            )
    )
    public ReadableColor ichor$init() {
        if (Ref.client().getMods().getReplayMod().getLunarUi().get()) {
            return ReadableColor.WHITE;
        }

        return ReadableColor.BLACK;
    }

    @Inject(
            method = "open",
            at = @At("HEAD")
    )
    public void ichor$open(CallbackInfo ci) {
        ReplayModGuiLink_v1_8.savingReplayOpen += 1;
    }

    @Inject(
            method = "close",
            at = @At("HEAD")
    )
    public void ichor$close(CallbackInfo ci) {
        ReplayModGuiLink_v1_8.savingReplayOpen -= 1;
        if (ReplayModGuiLink_v1_8.savingReplayOpen < 0) {
            ReplayModGuiLink_v1_8.savingReplayOpen = 0;
        }
    }
}
