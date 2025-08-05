package com.moonsworth.lunar.replaymod.v1_21_0.mixin;

import com.moonsworth.lunar.client.util.Ref;
import com.replaymod.render.capturer.*;
import com.replaymod.render.rendering.Frame;
import com.replaymod.render.utils.ByteBufferPool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.swing.JOptionPane;
import java.nio.ByteBuffer;

@Mixin(PboOpenGlFrameCapturer.class)
public abstract class PboOpenGlFrameCapturerMixin_v1_20<F extends Frame, D extends Enum<D> & CaptureData> extends OpenGlFrameCapturer<F, D> {

    public PboOpenGlFrameCapturerMixin_v1_20(WorldRenderer worldRenderer, RenderInfo renderInfo) {
        super(worldRenderer, renderInfo);
    }

    @Redirect(
            method = "readFromPbo",
            at = @At(
                    target = "Lcom/replaymod/render/utils/ByteBufferPool;allocate(I)Ljava/nio/ByteBuffer;",
                    value = "INVOKE"
            )
    )
    public ByteBuffer ichor$read(int frameBufferSize) {
        ByteBuffer frameBuffer;
        try {
            frameBuffer = ByteBufferPool.allocate(frameBufferSize);
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
            shutdown();
            return null;
        }
        return frameBuffer;
    }

    private void shutdown() {
        (new Thread(() -> JOptionPane.showMessageDialog(null,
                "Your client has ran out of memory while rendering a video.\nYou can increase memory allocation in the launcher.\n Another common fix is to reduce the Anti-Aliasing to 2x or 4x before rendering the replay.",
                "Out of Memory",
                JOptionPane.WARNING_MESSAGE))).start();

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Ref.mc().bridge$shutdownMinecraftApplet();
    }
}
