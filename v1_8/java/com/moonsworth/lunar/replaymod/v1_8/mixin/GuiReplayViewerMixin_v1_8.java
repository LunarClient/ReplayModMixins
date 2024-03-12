package com.moonsworth.lunar.replaymod.v1_8.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.moonsworth.lunar.replaymod.v1_8.link.ReplayModGuiLink_v1_8;
import com.replaymod.core.ReplayMod;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.GuiContainer;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.VanillaGuiScreen;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiLabel;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.Consumer;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadablePoint;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import com.replaymod.replaystudio.replay.ReplayMetaData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(GuiReplayViewer.GuiReplayList.class)
public abstract class GuiReplayViewerMixin_v1_8 {

    @Shadow protected abstract GuiReplayViewer.GuiReplayList getThis();

    @ModifyExpressionValue(
            method = "lambda$new$3(Lcom/replaymod/lib/de/johni0702/minecraft/gui/utils/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lcom/replaymod/replaystudio/replay/ReplayFile;getMetaData()Lcom/replaymod/replaystudio/replay/ReplayMetaData;")
    )
    private ReplayMetaData ichor$onReplayLoad(ReplayMetaData metaData) {
        if (metaData.getDuration() == 0) {
            throw new IllegalStateException("[Lunar] Invalid replay detected, with zero length, skipping: " + metaData);
        }

        return metaData;
    }

    @Inject(
            method = "typeKey",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$typeKey(ReadablePoint mousePosition, int keyCode, char keyChar, boolean ctrlDown, boolean shiftDown, CallbackInfoReturnable<Boolean> ci) {
        if (keyCode == 0x01) {
            getThis().getMinecraft().displayGuiScreen(null);
            ci.setReturnValue(true);
            return;
        }

        // We have a save option open
        if (ReplayModGuiLink_v1_8.savingReplayOpen > 0) {
            ci.setReturnValue(false);
        }
    }
}
