package com.moonsworth.lunar.replaymod.v1_12.mixin;

import com.moonsworth.lunar.bridge.BridgeManager;
import com.moonsworth.lunar.bridge.client.renderer.RenderContextLegacy;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.gui.framework.LCUI;
import com.moonsworth.lunar.client.management.managers.Fonts;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.OffsetGuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.RenderInfo;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiElement;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiSlider;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableDimension;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractGuiSlider.class)
public abstract class AbstractGuiSliderMixin_v1_12 extends AbstractGuiElement {

    @Shadow public String text;

    @Shadow public int value;

    @Shadow public int steps;

    @Inject(
            method = "draw",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void draw(GuiRenderer renderer, ReadableDimension size, RenderInfo renderInfo, CallbackInfo ci) {
        var renderContext = RenderContextLegacy.ofLegacy();

        boolean isLunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        if (!isLunarUI) {
            return;
        }
        super.draw(renderer, size, renderInfo);
        int width = size.getWidth();
        int height = size.getHeight();
        float offsetX = 0, offsetY = 0;
        int sliderX = (width - 8) * value / steps;
        if (renderer instanceof OffsetGuiRenderer) {
            offsetX = renderer.getOpenGlOffset().getX();
            offsetY = renderer.getOpenGlOffset().getY();
        }
        LCUI.btn(renderContext, offsetX, offsetY, size.getWidth(), size.getHeight(), 4F, 0x99000000);

        Fonts.getRoboto12Bold().drawCenteredStringWithShadow(renderContext, text, offsetX + width / 2F, offsetY + 1F, 0xFFFFFFFF);

        // Draw slider
        LCUI.rect(renderContext, offsetX + 3f, offsetY + height - 7.5F, width - 6F, 3F, 0xAF1A1B1B);
        LCUI.drawCircle(renderContext, offsetX + sliderX + 4, offsetY + height - 6, 4F, 0xFF4f94fc);
        BridgeManager.getGlStateManagerBridge().bridge$color(1F, 1F, 1F, 1F);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        ci.cancel();
    }

}
