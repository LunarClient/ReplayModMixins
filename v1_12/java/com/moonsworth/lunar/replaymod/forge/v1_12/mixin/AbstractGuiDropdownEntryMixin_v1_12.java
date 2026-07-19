package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.moonsworth.lunar.bridge.client.renderer.RenderContextLegacy;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.gui.framework.LCUI;
import com.moonsworth.lunar.client.management.managers.Fonts;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiClickable;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableColor;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.replaymod.lib.de.johni0702.minecraft.gui.element.advanced.AbstractGuiDropdownMenu$DropdownEntry")
public abstract class AbstractGuiDropdownEntryMixin_v1_12 extends AbstractGuiClickable {

    @Redirect(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/GuiRenderer;drawRect(IIIILcom/replaymod/lib/de/johni0702/minecraft/gui/utils/lwjgl/ReadableColor;)V"
            )
    )
    public void ichor$draw$rect(GuiRenderer instance, int x, int y, int w, int h, ReadableColor readableColor) {
        var renderContext = RenderContextLegacy.ofLegacy();

        boolean isLunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        if (!isLunarUI) {
            instance.drawRect(x, y, w, h, readableColor);
            return;
        }
        if (readableColor == ReadableColor.BLACK) {
            LCUI.btn(renderContext, instance.getOpenGlOffset().getX(), instance.getOpenGlOffset().getY(), w + 2, h + 1, 4F, 0xDF000000);
        }
    }

    @Redirect(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/GuiRenderer;drawString(IILcom/replaymod/lib/de/johni0702/minecraft/gui/utils/lwjgl/ReadableColor;Ljava/lang/String;)I"
            )
    )
    public int ichor$draw$string(GuiRenderer instance, int x, int y, ReadableColor readableColor, String s) {
        var renderContext = RenderContextLegacy.ofLegacy();

        boolean isLunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        if (!isLunarUI) {
            return instance.drawString(x, y, readableColor, s);
        }
        Fonts.getRoboto12Bold().drawStringWithShadow(renderContext, s, instance.getOpenGlOffset().getX() + 3F, instance.getOpenGlOffset().getY() + 3F, 0xFFFFFFFF);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        return 0;
    }

}
