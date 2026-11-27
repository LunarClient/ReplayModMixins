package com.moonsworth.lunar.replaymod.v1_20_6.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moonsworth.lunar.bridge.BridgeManager;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.feature.mod.replaymod.ReplayMod;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiElement;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.advanced.AbstractGuiTimeline;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractGuiTimeline.class)
public abstract class AbstractGuiTimelineMixin_v1_20<T extends AbstractGuiTimeline<T>> extends AbstractGuiElement<T> {

    @WrapOperation(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/GuiRenderer;bindTexture(Lnet/minecraft/resources/ResourceLocation;)V"
            )
    )
    public void ichor$draw$bindTexture(GuiRenderer instance, ResourceLocation resourceLocation,
                                       Operation<Void> original) {
        var lunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        if (lunarUI) {
            BridgeManager.getRenderSystem().bridge$color(1F, 1F, 1F, 1F);
            original.call(instance, (ResourceLocation) ReplayMod.LUNAR_UI);
            return;
        }
        original.call(instance, TEXTURE);
    }

    @WrapOperation(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/utils/Utils;drawDynamicRect(Lcom/replaymod/lib/de/johni0702/minecraft/gui/GuiRenderer;IIIIIIIIII)V"
            )
    )
    public void ichor$draw$dynamicRect(GuiRenderer guiRenderer, int width, int height, int texX, int texY, int texWidth,
                                       int texHeight, int borderTop, int borderBottom, int borderLeft, int borderRight,
                                       Operation<Void> original) {
        var lunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        if (lunarUI) {
            width += 3;
            texHeight *= 2;
            borderTop += 2;
            borderBottom = 0;
            borderLeft += 2;
            borderRight += 6;
        }
        original.call(guiRenderer, width, height, texX, texY, texWidth, texHeight,
                borderTop, borderBottom, borderLeft, borderRight);
    }

    @WrapOperation(
            method = "drawTimelineCursor",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/GuiRenderer;bindTexture(Lnet/minecraft/resources/ResourceLocation;)V"
            )
    )
    public void ichor$draw$Timeline(GuiRenderer instance, ResourceLocation resourceLocation, Operation<Void> original) {
        var lunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        if (lunarUI) {
            original.call(instance, (ResourceLocation) ReplayMod.LUNAR_UI);
            return;
        }
        original.call(instance, TEXTURE);
    }
}
