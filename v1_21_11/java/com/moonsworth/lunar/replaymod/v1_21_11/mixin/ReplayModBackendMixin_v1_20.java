package com.moonsworth.lunar.replaymod.v1_21_11.mixin;

import com.moonsworth.lunar.client.external.ExternalLinks;
import com.moonsworth.lunar.replaymod.v1_21_11.link.ReplayModGuiLink_Impl;
import com.replaymod.core.ReplayModBackend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReplayModBackend.class)
public class ReplayModBackendMixin_v1_20 {

    @Inject(method = "onInitializeClient", at = @At("HEAD"))
    private void lunar$onInitializeClient(CallbackInfo ci) {
        ExternalLinks.registerLink(new ReplayModGuiLink_Impl());
    }

}
