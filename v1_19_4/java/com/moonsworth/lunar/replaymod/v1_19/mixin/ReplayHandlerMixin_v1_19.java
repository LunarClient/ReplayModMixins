package com.moonsworth.lunar.replaymod.v1_19.mixin;

import com.replaymod.replay.FullReplaySender;
import com.replaymod.replay.ReplayHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReplayHandler.class)
public class ReplayHandlerMixin_v1_19 {

    @Shadow public FullReplaySender fullReplaySender;

    @Inject(
            method = "doJump",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$doJump(int targetTime, boolean cam, CallbackInfo ci) {
        FullReplaySender replaySender = this.fullReplaySender;
        if (replaySender.replayLength == targetTime) {
            ci.cancel();
        }
    }

}
