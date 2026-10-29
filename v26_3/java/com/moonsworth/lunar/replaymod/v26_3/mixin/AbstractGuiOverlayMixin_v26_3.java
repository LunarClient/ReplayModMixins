package com.moonsworth.lunar.replaymod.v26_3.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moonsworth.lunar.replaymod.v26_3.link.ReplayModGuiLink_Impl;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiOverlay;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractGuiOverlay.class)
public class AbstractGuiOverlayMixin_v26_3 {

    @WrapOperation(
            method = "updateUserInputGui",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"
            )
    )
    public void ichor$updateUserInput(Gui instance, Screen guiScreen, Operation<Void> original) {
        ReplayModGuiLink_Impl.setInputOverlayOpen(guiScreen != null);
        original.call(instance, guiScreen);
    }
}
