package com.moonsworth.lunar.replaymod.v1_17.mixin;

import com.moonsworth.lunar.client.external.ExternalLinks;
import com.moonsworth.lunar.client.external.replaymod.ReplayModLink;
import com.replaymod.core.KeyBindingRegistry;
import com.replaymod.recording.ReplayModRecording;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ReplayModRecording.class)
public abstract class ReplayModRecordingMixin_v1_17 {
    @Redirect(method = "registerKeyBindings", at = @At(value = "INVOKE", target = "Lcom/replaymod/core/KeyBindingRegistry;registerKeyBinding(Ljava/lang/String;ILjava/lang/Runnable;Z)Lcom/replaymod/core/KeyBindingRegistry$Binding;"))
    private KeyBindingRegistry.Binding lunar$registerKeyBindings(KeyBindingRegistry instance, String name, int keyCode, Runnable whenPressed, boolean onlyInRepay) {
        return instance.registerKeyBinding(name, keyCode, () -> {
            if (ExternalLinks.get(ReplayModLink.class).map(ReplayModLink::isRecording).orElse(true)) {
                whenPressed.run();
            }
        }, onlyInRepay);
    }
}
