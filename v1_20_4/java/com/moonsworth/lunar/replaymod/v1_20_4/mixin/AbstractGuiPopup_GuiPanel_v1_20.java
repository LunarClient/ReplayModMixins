package com.moonsworth.lunar.replaymod.v1_20_4.mixin;

import com.moonsworth.lunar.bridge.BridgeManager;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.feature.mod.replaymod.ReplayMod;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiContainer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.replaymod.lib.de.johni0702.minecraft.gui.popup.AbstractGuiPopup$2")
public abstract class AbstractGuiPopup_GuiPanel_v1_20 extends AbstractGuiContainer {

    /**
     * @author Alexandre
     * @reason Use Lunar UI texture
     */
    @Redirect(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/GuiRenderer;bindTexture(Lnet/minecraft/resources/ResourceLocation;)V"
            )
    )
    public void ichor$draw$bindTexture(GuiRenderer instance, ResourceLocation resourceLocation) {
        var lunarUI = Lunar.getClient().getMods().getReplayMod().getLunarUi().get();
        BridgeManager.getGlStateManagerBridge().bridge$color(1F, 1F, 1F, 1F);
        BridgeManager.getGlStateManagerBridge().bridge$enableBlend();
        BridgeManager.getGlStateManagerBridge().bridge$tryBlendFuncSeparate(770, 771, 1, 0);
        BridgeManager.getGlStateManagerBridge().bridge$enableAlpha();
        BridgeManager.getGlStateManagerBridge().bridge$alphaFunc(516, 0F);
        BridgeManager.getGlStateManagerBridge().bridge$disableDepth();
        if (lunarUI) {
            instance.bindTexture((ResourceLocation) ReplayMod.LUNAR_UI);
            return;
        }
        instance.bindTexture(TEXTURE);
    }

}
