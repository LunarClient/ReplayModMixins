package com.moonsworth.lunar.replaymod.forge.v1_8.mixin;

import com.moonsworth.lunar.client.external.ExternalLinks;
import com.moonsworth.lunar.replaymod.forge.v1_8.link.ReplayModGuiLink_v1_8;
import com.replaymod.core.ReplayModBackend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReplayModBackend.class)
public class ReplayModBackendMixin_v1_8 {

    @Inject(method = "init(Lnet/minecraftforge/fml/common/event/FMLPreInitializationEvent;)V", at = @At("HEAD"))
    private void onInitializeClient(CallbackInfo ci) {
        ExternalLinks.registerLink(new ReplayModGuiLink_v1_8());
        System.out.println("ReplayModBackendMixin_v1_8.init: initializing ReplayMod");
    }

}
