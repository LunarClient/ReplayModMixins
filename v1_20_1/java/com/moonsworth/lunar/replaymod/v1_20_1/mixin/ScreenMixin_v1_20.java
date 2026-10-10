package com.moonsworth.lunar.replaymod.v1_20_1.mixin;

import com.google.common.collect.Collections2;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.InitScreenCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin_v1_20 {

    @Shadow @Final public List<GuiEventListener> children;

    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At("HEAD"))
    private void preInit(Minecraft minecraftClient_1, int int_1, int int_2, CallbackInfo ci) {
        InitScreenCallback.Pre.EVENT.invoker().preInitScreen((Screen) (Object) this);
    }

    @Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At("RETURN"))
    private void init(Minecraft minecraftClient_1, int int_1, int int_2, CallbackInfo ci) {
        InitScreenCallback.EVENT.invoker().initScreen(
                (Screen) (Object) this,
                 Collections2.transform(Collections2.filter(this.children, it -> it instanceof AbstractButton), it -> (AbstractButton) it)
        );
    }
}
