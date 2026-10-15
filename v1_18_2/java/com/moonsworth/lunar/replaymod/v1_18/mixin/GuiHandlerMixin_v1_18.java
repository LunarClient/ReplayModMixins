package com.moonsworth.lunar.replaymod.v1_18.mixin;

import com.moonsworth.lunar.client.util.Ref;
import com.replaymod.recording.handler.GuiHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiHandler.class)
public class GuiHandlerMixin_v1_18 {

    @Inject(
            method = "onGuiInit",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void ichor$onGuiInit(Screen settingsRegistry, CallbackInfo ci) {
        if (Ref.client() != null && Ref.client().getMods() != null && !Ref.client().getMods().getReplayMod().isEnabled()) {
            ci.cancel();
        }
    }

}
