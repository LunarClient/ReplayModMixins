package com.moonsworth.lunar.replaymod.v1_8.mixin;

import com.moonsworth.lunar.replaymod.v1_8.link.ReplayModGuiLink_v1_8;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractGuiOverlay.class)
public class AbstractGuiOverlayMixin_v1_8 {

    @Redirect(
            method = "updateUserInputGui",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"
            )
    )
    public void ichor$updateUserInput(Minecraft instance, GuiScreen guiScreen) {
        ReplayModGuiLink_v1_8.hasInputOverlayOpen = guiScreen != null;
        instance.displayGuiScreen(guiScreen);
    }
}
