package com.moonsworth.lunar.replaymod.v1_16.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.vector.Vector3f;
import com.replaymod.replaystudio.pathing.path.Keyframe;
import com.replaymod.simplepathing.preview.PathPreviewRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathPreviewRenderer.class)
public abstract class PathPreviewRendererMixin_v1_16 {

    @Inject(
            method = "drawPoint",
            at = @At(
                    value = "HEAD"
            )
    )
    public void ichor$drawPoint(Vector3f view, Vector3f pos, Keyframe keyframe, CallbackInfo ci) {
        RenderSystem.enableAlphaTest();
    }

}
