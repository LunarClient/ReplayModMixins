package com.moonsworth.lunar.replaymod.v1_20_1.mixin;

import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.replaymod.v1_20_1.link.ReplayModGuiLink_Impl;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableColor;
import com.replaymod.recording.gui.GuiSavingReplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSavingReplay.class)
public class GuiSavingReplayMixin_v1_20 {

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
        ReplayModGuiLink_Impl.savingReplayOpen += 1;
    }

    @Inject(
            method = "close",
            at = @At("HEAD")
    )
    public void ichor$close(CallbackInfo ci) {
        ReplayModGuiLink_Impl.savingReplayOpen -= 1;
        if (ReplayModGuiLink_Impl.savingReplayOpen < 0) {
            ReplayModGuiLink_Impl.savingReplayOpen = 0;
        }
    }
}
