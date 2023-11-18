package com.moonsworth.lunar.replaymod.v1_8.mixin;

import com.replaymod.replay.ReplayHandler;
import com.replaymod.replaystudio.replay.ReplayFile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(ReplayHandler.class)
public class ReplayHandlerMixin_v1_8 {

    int length = 0;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    public void ichor$init(ReplayFile replayFile, boolean asyncMode, CallbackInfo ci) {
        try {
            length = replayFile.getMetaData().getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Inject(
            method = "doJump",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$doJump(int targetTime, boolean cam, CallbackInfo ci) {
        if (length == targetTime) {
            ci.cancel();
        }
    }

}
