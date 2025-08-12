package com.moonsworth.lunar.replaymod.v1_20_1.mixin;

import com.moonsworth.lunar.client.gui.framework.LCUI;
import com.moonsworth.lunar.client.gui.framework.mainmenu.MainMenuBaseLCUI;
import com.moonsworth.lunar.modern.wrapper.client.gui.ScreenInjector;
import com.replaymod.core.gui.GuiBackgroundProcesses;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiBackgroundProcesses.class)
public class GuiBackgroundProcessMixin_v1_20 {

    @Inject(
            method = "onGuiInit",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$onGuiInit(Screen guiScreen, CallbackInfo callbackInfo) {
        if (guiScreen instanceof ScreenInjector && ((ScreenInjector) guiScreen).getCustomScreen() instanceof LCUI && !(((ScreenInjector) guiScreen).getCustomScreen() instanceof MainMenuBaseLCUI)) {
            callbackInfo.cancel();
        }
    }
}
