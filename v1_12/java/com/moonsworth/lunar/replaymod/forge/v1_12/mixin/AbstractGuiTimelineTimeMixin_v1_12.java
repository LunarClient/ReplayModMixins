package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.moonsworth.lunar.bridge.client.renderer.RenderContextLegacy;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.management.managers.Fonts;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.OffsetGuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.advanced.AbstractGuiTimelineTime;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractGuiTimelineTime.class)
public abstract class AbstractGuiTimelineTimeMixin_v1_12 {

    @Redirect(
            method = "drawTime",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/GuiRenderer;drawCenteredString(IILcom/replaymod/lib/de/johni0702/minecraft/gui/utils/lwjgl/ReadableColor;Ljava/lang/String;Z)I"
            )
    )
    public int ichor$draw$bindTexture(GuiRenderer instance, int positionX, int positionY, ReadableColor readableColor, String s, boolean b) {
        var lunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        if (lunarUI) {
            var renderContext = RenderContextLegacy.ofLegacy();

            float offsetPositionX = 0F;
            float offsetPositionY = 0F;
            if (instance instanceof OffsetGuiRenderer) {
                offsetPositionX += instance.getOpenGlOffset().getX();
                offsetPositionY += instance.getOpenGlOffset().getY();
            }
            Fonts.getRoboto13Medium().drawCenteredStringWithShadow(renderContext, s, positionX + offsetPositionX, offsetPositionY, 0xFFFFFFFF);
            return 0;
        }
        return instance.drawCenteredString(positionX, positionY, readableColor, s, b);
    }
}
