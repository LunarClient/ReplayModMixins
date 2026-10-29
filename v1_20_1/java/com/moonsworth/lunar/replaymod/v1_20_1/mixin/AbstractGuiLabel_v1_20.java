package com.moonsworth.lunar.replaymod.v1_20_1.mixin;

import com.moonsworth.lunar.client.Lunar;
import com.replaymod.lib.de.johni0702.minecraft.gui.element.AbstractGuiLabel;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractGuiLabel.class)
public abstract class AbstractGuiLabel_v1_20<T extends AbstractGuiLabel<T>> {

    /**
     * @author Alexandre
     * @reason The Lunar UI has a black background, so we don't want to draw black text
     */
    @Inject(
            method = "setColor(Lcom/replaymod/lib/de/johni0702/minecraft/gui/utils/lwjgl/ReadableColor;)Lcom/replaymod/lib/de/johni0702/minecraft/gui/element/AbstractGuiLabel;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$setColor(ReadableColor color, CallbackInfoReturnable<T> cir) {
        if (Lunar.getClient().getMods().getReplayMod().getLunarUi().get() && color == ReadableColor.BLACK) {
            cir.setReturnValue((T) (Object) this);
        }
    }

}
