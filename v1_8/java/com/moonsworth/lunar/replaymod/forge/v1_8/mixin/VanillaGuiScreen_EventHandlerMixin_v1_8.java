package com.moonsworth.lunar.replaymod.forge.v1_8.mixin;

import com.moonsworth.lunar.client.ui.osr.GameUI;
import net.minecraftforge.client.event.GuiScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.replaymod.lib.de.johni0702.minecraft.gui.container.VanillaGuiScreen$EventHandler")
public abstract class VanillaGuiScreen_EventHandlerMixin_v1_8 {

    @Inject(method = "onMouseInput", at = @At("HEAD"), cancellable = true)
    private void webosr$cancelMouse(GuiScreenEvent.MouseInputEvent.Pre var1, CallbackInfo ci) {
        if (GameUI.getInstance() != null && GameUI.getInstance().handleInput()) {
            ci.cancel();
        }
    }

    @Inject(method = "onKeyboardInput", at = @At("HEAD"), cancellable = true)
    private void webosr$cancelKeyboard(GuiScreenEvent.KeyboardInputEvent.Pre var1, CallbackInfo ci) {
        if (GameUI.getInstance() != null && GameUI.getInstance().handleInput()) {
            ci.cancel();
        }
    }

    @Redirect(method = "onMouseInput", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/event/GuiScreenEvent$MouseInputEvent$Pre;setCanceled(Z)V"))
    private void ichor$dontCancelMouse(GuiScreenEvent.MouseInputEvent.Pre instance, boolean b) {
        // no op
    }

    @Redirect(method = "onKeyboardInput", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/event/GuiScreenEvent$KeyboardInputEvent$Pre;setCanceled(Z)V"))
    private void ichor$dontCancelKeyboard(GuiScreenEvent.KeyboardInputEvent.Pre instance, boolean b) {
        // no op
    }
}
