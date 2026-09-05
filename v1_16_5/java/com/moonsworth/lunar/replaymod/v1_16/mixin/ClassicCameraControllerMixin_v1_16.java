package com.moonsworth.lunar.replaymod.v1_16.mixin;

import com.moonsworth.lunar.client.util.MathUtil;
import com.replaymod.replay.camera.ClassicCameraController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClassicCameraController.class)
public class ClassicCameraControllerMixin_v1_16 {

    @Shadow public double MAX_SPEED;

    @Shadow public static double LOWER_SPEED;

    @Shadow public static double UPPER_SPEED;

    @Shadow public double THRESHOLD;

    @Shadow public double DECAY;

    /**
     * @author Tre
     * @reason Completely overdo their speed controller
     */
    @Overwrite
    public void setCameraMaximumSpeed(double maxSpeed) {
        this.MAX_SPEED = MathUtil.limit(maxSpeed, LOWER_SPEED, UPPER_SPEED);
        this.THRESHOLD = this.MAX_SPEED / 20.0D;
        this.DECAY = 5.0D;
    }
}
