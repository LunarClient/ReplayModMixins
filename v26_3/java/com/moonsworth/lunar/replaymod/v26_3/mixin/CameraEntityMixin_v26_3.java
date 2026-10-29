package com.moonsworth.lunar.replaymod.v26_3.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.replaymod.core.utils.Utils;
import com.replaymod.replay.camera.CameraEntity;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CameraEntity.class)
public class CameraEntityMixin_v26_3 {

    private long lastUpdate;

    @Shadow public float roll;

    /**
     * @reason Fix camera roll being framerate dependent
     */
    @WrapOperation(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/KeyMapping;isDown()Z"
            ),
            expect = 2,
            require = 2
    )
    public boolean ichor$update(KeyMapping instance, Operation<Boolean> original) {
        long now = System.currentTimeMillis();
        if (original.call(instance)) {
            long timePassed = now - this.lastUpdate;
            if (instance.getName().contains("rollclockwise")) {
                roll += (Utils.isCtrlDown() ? 0.2 : 1) * timePassed * 0.05f;
            } else {
                roll -= (Utils.isCtrlDown() ? 0.2 : 1) * timePassed * 0.05f;
            }
        }
        return false;
    }

    /**
     * @reason Register last update time after camera update
     */
    @Inject(
            method = "update",
            at = @At("TAIL")
    )
    public void ichor$update$lastUpdate(CallbackInfo ci) {
        this.lastUpdate = System.currentTimeMillis();
    }

}
