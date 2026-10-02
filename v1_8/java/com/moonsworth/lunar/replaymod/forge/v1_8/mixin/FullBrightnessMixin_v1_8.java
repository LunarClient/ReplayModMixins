package com.moonsworth.lunar.replaymod.forge.v1_8.mixin;

import com.replaymod.core.SettingsRegistry;
import com.replaymod.extras.FullBrightness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FullBrightness.class)
public class FullBrightnessMixin_v1_8 {

    @Redirect(
            method = "getType",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/SettingsRegistry;get(Lcom/replaymod/core/SettingsRegistry$SettingKey;)Ljava/lang/Object;"
            )
    )
    public <T> T ichor$getType(SettingsRegistry instance, SettingsRegistry.SettingKey<T> key) {
        return (T) "Gamma";
    }

}
