package com.moonsworth.lunar.replaymod.forge.v1_8.mixin;

import com.moonsworth.lunar.client.util.MathUtil;
import com.replaymod.replay.camera.ClassicCameraController;
import org.spongepowered.asm.mixin.*;

@Mixin(ClassicCameraController.class)
public class ClassicCameraControllerMixin_v1_8 {

    @Shadow private double MAX_SPEED;

    @Final
    @Shadow private static double LOWER_SPEED;

    @Final
    @Shadow private static double UPPER_SPEED;

    @Shadow private double THRESHOLD;

    @Shadow private double DECAY;

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
