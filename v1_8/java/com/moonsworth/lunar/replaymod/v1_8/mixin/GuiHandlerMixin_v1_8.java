package com.moonsworth.lunar.replaymod.v1_8.mixin;

import com.moonsworth.lunar.client.util.Ref;
import com.replaymod.recording.handler.GuiHandler;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiHandler.class)
public class GuiHandlerMixin_v1_8 {

    @Inject(
            method = "onGuiInit",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void ichor$onGuiInit(GuiScreen settingsRegistry, CallbackInfo ci) {
        if (Ref.client() != null && Ref.client().getMods() != null && !Ref.client().getMods().getReplayMod().isEnabled()) {
            ci.cancel();
        }
    }

}
