package com.moonsworth.lunar.replaymod.v1_21_0.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractGuiElement.class)
public class AbstractGuiElement_v1_20 {

    @Shadow public static net.minecraft.resources.ResourceLocation TEXTURE;

    @Inject(
            method = "<init>()V",
            at = @At("TAIL")
    )
    public void ichor$init(CallbackInfo ci) {
        TEXTURE = new net.minecraft.resources.ResourceLocation("lunar", "replaymod/gui.png");
    }
}
