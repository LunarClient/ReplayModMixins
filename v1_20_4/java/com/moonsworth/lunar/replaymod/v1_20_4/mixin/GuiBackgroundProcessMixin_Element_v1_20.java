package com.moonsworth.lunar.replaymod.v1_20_4.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moonsworth.lunar.bridge.BridgeManager;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.feature.mod.replaymod.ReplayMod;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiContainer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "com.replaymod.core.gui.GuiBackgroundProcesses$Element")
public abstract class GuiBackgroundProcessMixin_Element_v1_20 extends AbstractGuiContainer {

    /**
     * @reason Use Lunar UI texture
     */
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
        BridgeManager.getRenderSystem().bridge$color(1F, 1F, 1F, 1F);
        BridgeManager.getRenderSystem().bridge$enableBlend();
        BridgeManager.getRenderSystem().bridge$tryBlendFuncSeparate(770, 771, 1, 0);
        BridgeManager.getRenderSystem().bridge$alphaFunc(516, 0F);
        if (lunarUI) {
            original.call(instance, (ResourceLocation) ReplayMod.LUNAR_UI);
            return;
        }
        original.call(instance, TEXTURE);
    }

}
