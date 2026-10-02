package com.moonsworth.lunar.replaymod.v26_2.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.feature.mod.replaymod.ReplayMod;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiElement;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiHorizontalScrollbar;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractGuiHorizontalScrollbar.class)
public abstract class AbstractGuiHorizontalScrollbarMixin_v1_20 extends AbstractGuiElement {

    @WrapOperation(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/GuiRenderer;bindTexture(Lnet/minecraft/resources/Identifier;)V"
            )
    )
    public void ichor$draw(GuiRenderer instance, Identifier identifier, Operation<Void> original) {
        if (Lunar.getClient().getMods().getReplayMod().getLunarUi().get()) {
            identifier = (Identifier) (Object) ReplayMod.LUNAR_UI;
        }

        original.call(instance, identifier);
    }
}
