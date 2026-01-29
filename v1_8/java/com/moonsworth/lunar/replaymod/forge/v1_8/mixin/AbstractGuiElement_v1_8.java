package com.moonsworth.lunar.replaymod.forge.v1_8.mixin;

import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiElement;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractGuiElement.class)
public class AbstractGuiElement_v1_8 {

    @Shadow
    @Final
    @Mutable
    protected static ResourceLocation TEXTURE;

    @Inject(
            method = "<init>()V",
            at = @At("TAIL")
    )
    public void ichor$init(CallbackInfo ci) {
        TEXTURE = new ResourceLocation("lunar", "replaymod/gui.png");
    }
}
