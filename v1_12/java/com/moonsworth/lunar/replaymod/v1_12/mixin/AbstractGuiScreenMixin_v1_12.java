package com.moonsworth.lunar.replaymod.v1_12.mixin;

import com.moonsworth.lunar.bridge.BridgeManager;
import com.moonsworth.lunar.bridge.client.renderer.RenderContextLegacy;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.event.EventBus;
import com.moonsworth.lunar.client.event.impl.world.EventTick;
import com.moonsworth.lunar.client.gui.framework.LCUI;
import com.moonsworth.lunar.client.management.managers.Fonts;
import com.moonsworth.lunar.client.util.Ref;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.OffsetGuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.RenderInfo;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiScreen;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.GuiLabel;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.Dimension;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.Point;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableDimension;
import net.minecraft.client.gui.GuiScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractGuiScreen.class)
public abstract class AbstractGuiScreenMixin_v1_12<T> {

    @Shadow private AbstractGuiScreen.Background background;
    @Shadow private GuiLabel title;
    @Shadow public abstract GuiScreen toMinecraft();
    private ReadableDimension size;
    private RenderInfo renderInfo;
    private static int lunarPanoramaTimer;

    static {
        EventBus.getBus().register(EventTick.class, eventTick -> lunarPanoramaTimer++);
    }

    @Inject(
            method = "draw",
            at = @At(
                    value = "HEAD"
            )
    )
    public void ichor$draw(GuiRenderer bottom, ReadableDimension titleSize, RenderInfo x, CallbackInfo ci) {
        this.size = titleSize;
        this.renderInfo = x;
    }

    /**
     * @author Tre
     * @reason A hack so that when it checks renderInfo.layer == 0, it'll return -4 so we can overwrite it...
     */
    @Redirect(
            method = "draw",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/RenderInfo;layer:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    public int ichor$layer(RenderInfo instance) {
        return -4;
    }

    @Inject(
            method = "draw",
            at = @At("HEAD")
    )
    public void ichor$draw$background(GuiRenderer bottom, ReadableDimension titleSize, RenderInfo x, CallbackInfo ci) {
        var renderContext = RenderContextLegacy.ofLegacy();
        if (renderInfo.layer == 0) {
            switch (this.background) {
                case NONE:
                    break;
                case DIRT:
                    if (!Lunar.getClient().getMods().getReplayMod().getLunarUi().get()) {
                        toMinecraft().drawBackground(0);
                        break;
                    }
                case DEFAULT:
                    if (Lunar.getClient().getMods().getReplayMod().getLunarUi().get()) {
                        if (Ref.world() == null) {
                            BridgeManager.getGlStateManagerBridge().bridge$disableAlpha();
                            LCUI.renderSkybox(renderContext, size.getWidth(), size.getHeight(), lunarPanoramaTimer, renderInfo.getPartialTick());
                            BridgeManager.getGlStateManagerBridge().bridge$enableAlpha();
                            break;
                        }
                    } else {
                        toMinecraft().drawDefaultBackground();
                        break;
                    }
                case TRANSPARENT:
                    int top = -1072689136;
                    int xConst = -804253680;
                    bottom.drawRect(0, 0, size.getWidth(), size.getHeight(), top, top, xConst, xConst);
                    break;

            }

            if (this.title != null) {
                int xConst = size.getWidth() / 2 - titleSize.getWidth() / 2;
                OffsetGuiRenderer eRenderer = new OffsetGuiRenderer(bottom, new Point(xConst, 10), new Dimension(0, 0));

                if (Lunar.getClient().getMods().getReplayMod().getLunarUi().get()) {
                    final String text = title.getText().toUpperCase().replace("", " ").trim();
                    Fonts.getRoboto14Bold().drawCenteredString(renderContext, text, size.getWidth() / 2F, 13, 0xFFFFFFFF);
                } else {
                    title.draw(eRenderer, titleSize, renderInfo);
                }
            }
        }
    }
}
