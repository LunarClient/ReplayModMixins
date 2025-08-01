package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.moonsworth.lunar.bridge.client.renderer.RenderContextLegacy;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.feature.mod.replaymod.ReplayMod;
import com.moonsworth.lunar.client.gui.framework.LCUI;
import com.moonsworth.lunar.client.gui.framework.animations.ColorInterpAnimation;
import com.moonsworth.lunar.client.gui.framework.font.CFontRenderer;
import com.moonsworth.lunar.client.management.managers.Fonts;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.OffsetGuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.RenderInfo;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiButton;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiClickable;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.IGuiButton;
import com.replaymod.lib.de.johni0702.minecraft.gui.function.Clickable;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.Point;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableDimension;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadablePoint;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractGuiButton.class)
public abstract class AbstractGuiButtonMixin_v1_12<T extends AbstractGuiButton<T>> extends AbstractGuiClickable<T> implements Clickable, IGuiButton<T> {

    private final ColorInterpAnimation borderColor = new ColorInterpAnimation(0x40252525, 0x40252525);
    private final ColorInterpAnimation backgroundColor = new ColorInterpAnimation(0x20FFFFFF, 0x45FFFFFF);

    @Shadow
    private ReadablePoint spriteUV;

    @Shadow
    private ReadableDimension spriteSize;

    @Shadow
    private ReadableDimension textureSize;

    @Shadow
    private String label;

    @Shadow
    private ResourceLocation texture;

    @Inject(
            method = "draw",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void draw(GuiRenderer renderer, ReadableDimension size, RenderInfo renderInfo, CallbackInfo ci) {
        var renderContext = RenderContextLegacy.ofLegacy();

        boolean isLunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        if (!isLunarUI || renderInfo.layer == 1) {
            return;
        }
        boolean hover = isMouseHovering(new Point(renderInfo.mouseX, renderInfo.mouseY));
        int color = 0x7FFFFFFF;
        if (!isEnabled()) {
            color = 0x808080;
        } else if (hover) {
            color = 0xffffa0;
        }

        super.draw(renderer, size, renderInfo);

        float offsetPositionX = 0F;
        float offsetPositionY = 0F;
        if (renderer instanceof OffsetGuiRenderer) {
            offsetPositionX += renderer.getOpenGlOffset().getX();
            offsetPositionY += renderer.getOpenGlOffset().getY();
        }

        GlStateManager.pushMatrix();

        int borderColor = this.borderColor.color(isEnabled() && hover);
        int backgroundColor = this.backgroundColor.color(isEnabled() && hover);

        LCUI.btn(renderContext, offsetPositionX + 1, offsetPositionY + 1, size.getWidth() - 2, size.getHeight() - 2, 4F, borderColor, 0x20FFFFFF, backgroundColor);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);

        if (this.texture != null) {
            renderer.bindTexture((ResourceLocation) ReplayMod.LUNAR_RM_UI);
            if (spriteUV != null && textureSize != null) {
                ReadableDimension spriteSize = this.spriteSize != null ? this.spriteSize : getMinSize();
                renderer.drawTexturedRect(0, 0, spriteUV.getX(), spriteUV.getY(), size.getWidth(), size.getHeight(),
                        spriteSize.getWidth(), spriteSize.getHeight(), textureSize.getWidth(), textureSize.getHeight());
            } else {
                renderer.drawTexturedRect(0, 0, 0, 0, size.getWidth(), size.getHeight());
            }
        }

        if (label != null) {
            CFontRenderer font = Fonts.getRoboto14Bold();
            font.drawString(renderContext, label.toUpperCase(), (offsetPositionX + size.getWidth() / 2.0F + 1) - font.getStringWidth(label.toUpperCase()) / 2F, offsetPositionY + size.getHeight() / 2.0F - font.getHeight() + 1, 0x20000000);
            font.drawString(renderContext, label.toUpperCase(), (offsetPositionX + size.getWidth() / 2.0F) - font.getStringWidth(label.toUpperCase()) / 2F, offsetPositionY + size.getHeight() / 2.0F - font.getHeight(), color);
        }

        GlStateManager.popMatrix();

        ci.cancel();
    }
}
