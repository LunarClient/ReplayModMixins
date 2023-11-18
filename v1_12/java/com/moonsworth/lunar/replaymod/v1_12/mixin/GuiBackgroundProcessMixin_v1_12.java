package com.moonsworth.lunar.replaymod.v1_12.mixin;

import com.moonsworth.lunar.client.gui.framework.LCUI;
import com.moonsworth.lunar.client.gui.framework.mainmenu.MainMenuBaseLCUI;
import com.moonsworth.lunar.v1_12.wrapper.client.gui.ScreenInjector;
import com.replaymod.core.gui.GuiBackgroundProcesses;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiBackgroundProcesses.class)
public class GuiBackgroundProcessMixin_v1_12 {

    @Inject(
            method = "onGuiInit",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$onGuiInit(GuiScreen guiScreen, CallbackInfo callbackInfo) {
        if (guiScreen instanceof ScreenInjector && ((ScreenInjector) guiScreen).getCustomScreen() instanceof LCUI && !(((ScreenInjector) guiScreen).getCustomScreen() instanceof MainMenuBaseLCUI)) {
            callbackInfo.cancel();
        }
    }
}
