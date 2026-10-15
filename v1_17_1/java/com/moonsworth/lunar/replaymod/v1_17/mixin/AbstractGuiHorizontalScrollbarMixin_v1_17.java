package com.moonsworth.lunar.replaymod.v1_17.mixin;

import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.feature.mod.replaymod.ReplayMod;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiElement;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiHorizontalScrollbar;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractGuiHorizontalScrollbar.class)
public abstract class AbstractGuiHorizontalScrollbarMixin_v1_17 extends AbstractGuiElement {

    @Redirect(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/GuiRenderer;bindTexture(Lnet/minecraft/resources/ResourceLocation;)V"
            )
    )
    public void ichor$draw(GuiRenderer instance, ResourceLocation resourceLocation) {
        instance.bindTexture(Lunar.getClient().getMods().getReplayMod().getLunarUi().get() ? (ResourceLocation) ReplayMod.LUNAR_UI : TEXTURE);
    }
}
