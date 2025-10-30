package com.moonsworth.lunar.replaymod.forge.v1_8.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.container.VanillaGuiScreen;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VanillaGuiScreen.class)
public abstract class VanillaGuiScreenMixin_v1_8 {

    @Shadow protected abstract GuiScreen getSuperMcGui();

    @Inject(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiScreen;setWorldAndResolution(Lnet/minecraft/client/Minecraft;II)V"
            )
    )
    public void ichor$register(CallbackInfo ci) {
        getSuperMcGui().initGui();
    }
}
