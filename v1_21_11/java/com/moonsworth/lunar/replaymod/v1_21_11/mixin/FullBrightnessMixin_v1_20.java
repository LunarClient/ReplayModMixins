package com.moonsworth.lunar.replaymod.v1_21_11.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.replaymod.core.SettingsRegistry;
import com.replaymod.extras.FullBrightness;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FullBrightness.class)
public class FullBrightnessMixin_v1_20 {

    @WrapOperation(
            method = "getType",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/SettingsRegistry;get(Lcom/replaymod/core/SettingsRegistry$SettingKey;)Ljava/lang/Object;"
            )
    )
    public Object ichor$getType(SettingsRegistry instance, SettingsRegistry.SettingKey<?> key,
                                Operation<Object> original) {
        return "Gamma";
    }

}
