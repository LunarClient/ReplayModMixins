package com.moonsworth.lunar.replaymod.v1_20_6.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.moonsworth.lunar.bridge.blaze3d.vertex.PoseStackBridge;
import com.moonsworth.lunar.bridge.client.renderer.RenderContextModern;
import com.moonsworth.lunar.bridge.util.ResourceLocationBridge;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.gui.framework.LCUI;
import com.moonsworth.lunar.client.management.managers.Fonts;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.RenderInfo;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractComposedGuiElement;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.advanced.AbstractGuiDropdownMenu;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;

@Mixin(AbstractGuiDropdownMenu.class)
public abstract class AbstractGuiDropdownMenuMixin_v1_20<V, T extends AbstractGuiDropdownMenu<V, T>> extends AbstractComposedGuiElement {

    @Shadow
    public Function<V, String> toString;

    @Shadow
    public abstract V getSelectedValue();

    private final ResourceLocationBridge downArrowIcon = ResourceLocationBridge.create("lunar", "icons/down-arrow-16x16.png");

    @Inject(
            method = "draw",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void draw(GuiRenderer renderer, ReadableDimension size, RenderInfo renderInfo, CallbackInfo ci) {
        var renderContext = RenderContextModern.builder().poseStack((PoseStackBridge) renderer.getMatrixStack()).legacyGlobalState(true).build();

        boolean isLunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        if (!isLunarUI || renderInfo.layer == 1) {
            return;
        }
        super.draw(renderer, size, renderInfo);
        if (renderInfo.layer == 0) {
            LCUI.btn(renderContext, renderer.getOpenGlOffset().getX(), renderer.getOpenGlOffset().getY(), size.getWidth(), size.getHeight(), 4F, 0x99000000);
            String s = toString.apply(getSelectedValue()).toUpperCase();
            float strWidth = Fonts.getRoboto14Bold().getStringWidth(s);
            if (strWidth >= size.getWidth() - 10F) {
                List<String> wrapped = Fonts.getRoboto10Bold().wrapWords(s, size.getWidth() - 10F);
                float height = 0F;
                for (String s1 : wrapped) {
                    Fonts.getRoboto10Bold().drawCenteredStringWithShadow(renderContext, s1, renderer.getOpenGlOffset().getX() + size.getWidth() / 2F - 4, renderer.getOpenGlOffset().getY() + 4 + height, 0xFFFFFFFF);
                    height += 6F;
                }
            } else {
                Fonts.getRoboto14Bold().drawCenteredStringWithShadow(renderContext, s, renderer.getOpenGlOffset().getX() + size.getWidth() / 2F - 4, renderer.getOpenGlOffset().getY() + (size.getHeight() - 8) / 2F, 0xFFFFFFFF);
            }
            LCUI.drawTexture(renderContext, downArrowIcon, 4F, renderer.getOpenGlOffset().getX() + size.getWidth() - 12F, renderer.getOpenGlOffset().getY() + 6F);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(770, 771, 1, 0);
            RenderSystem.blendFunc(770, 771);
        }

        ci.cancel();
    }

}
