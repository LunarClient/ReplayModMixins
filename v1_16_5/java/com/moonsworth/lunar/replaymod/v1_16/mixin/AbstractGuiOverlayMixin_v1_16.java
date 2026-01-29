package com.moonsworth.lunar.replaymod.v1_16.mixin;

import com.moonsworth.lunar.replaymod.v1_16.link.ReplayModGuiLink_Impl;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractGuiOverlay.class)
public class AbstractGuiOverlayMixin_v1_16 {

    @Redirect(
            method = "updateUserInputGui",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"
            )
    )
    public void ichor$updateUserInput(Minecraft instance, Screen guiScreen) {
        ReplayModGuiLink_Impl.hasInputOverlayOpen = guiScreen != null;
        instance.setScreen(guiScreen);
    }
}
