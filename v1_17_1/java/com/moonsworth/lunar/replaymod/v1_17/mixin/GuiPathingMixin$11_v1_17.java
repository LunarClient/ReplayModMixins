package com.moonsworth.lunar.replaymod.v1_17.mixin;

import com.replaymod.simplepathing.gui.GuiPathing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.replaymod.simplepathing.gui.GuiPathing$10")
public class GuiPathingMixin$11_v1_17 {

    @Shadow
    public GuiPathing this$0;

    @Inject(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/util/concurrent/ListenableFuture;cancel(Z)Z",
                    shift = At.Shift.AFTER
            )
    )
    public void ichor$tickPlayer(CallbackInfo ci) {
        // Tear down of the player might only happen the next tick after it was cancelled
        this.this$0.player.onTick();
    }
}
