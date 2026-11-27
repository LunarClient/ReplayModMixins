package com.moonsworth.lunar.replaymod.v1_20_2.mixin;

import com.replaymod.core.ReplayMod;
import com.replaymod.simplepathing.SPTimeline;
import com.replaymod.simplepathing.Setting;
import com.replaymod.simplepathing.gui.GuiPathing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiPathing.class)
public class GuiPathingMixin_v1_20 {

    @Inject(
            method = "toggleKeyframe",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/simplepathing/gui/GuiKeyframeTimeline;getCursorPosition()I"
            )
    )
    public void ichor$toggleKeyframe(SPTimeline.SPPath camera, boolean spectatedId, CallbackInfo ci) {
        if (!ReplayMod.instance.getSettingsRegistry().get(Setting.PATH_PREVIEW)) {
            ReplayMod.instance.printWarningToChat("Keyframe set although path preview is currently hidden.");
        }
    }
}
