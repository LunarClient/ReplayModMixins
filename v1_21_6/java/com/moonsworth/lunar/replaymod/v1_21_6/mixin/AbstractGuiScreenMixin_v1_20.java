package com.moonsworth.lunar.replaymod.v1_21_6.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moonsworth.lunar.client.event.EventBus;
import com.moonsworth.lunar.client.event.impl.world.EventTick;
import com.replaymod.lib.de.johni0702.minecraft.gui.GuiRenderer;
import com.replaymod.lib.de.johni0702.minecraft.gui.RenderInfo;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiScreen;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.lwjgl.ReadableDimension;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractGuiScreen.class)
public abstract class AbstractGuiScreenMixin_v1_20<T> {

    private ReadableDimension lunar$size;
    private RenderInfo lunar$renderInfo;
    private static int lunar$panoramaTimer;

    static {
        EventBus.getBus().register(EventTick.class, eventTick -> {
            lunar$panoramaTimer++;
        });
    }

    @ModifyVariable(
            method = "layout",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private ReadableDimension ichor$useInternalSize(ReadableDimension size) {
        if (size == null) {
            return this.lunar$size;
        }
        return size;
    }

    @Inject(
            method = "draw",
            at = @At(
                    value = "HEAD"
            )
    )
    public void ichor$draw(GuiRenderer bottom, ReadableDimension titleSize, RenderInfo x, CallbackInfo ci) {
        this.lunar$size = titleSize;
        this.lunar$renderInfo = x;
    }

    /**
     * @author Tre
     * @reason A hack so that when it checks renderInfo.layer == 0, it'll return -4 so we can overwrite it...
     */
    @WrapOperation(
            method = "draw",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/replaymod/lib/de/johni0702/minecraft/gui/RenderInfo;layer:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0
            )
    )
    public int ichor$layer(RenderInfo instance, Operation<Integer> original) {
        return -4;
    }

}
