package com.moonsworth.lunar.replaymod.v1_8.mixin;

import com.moonsworth.lunar.client.gui.framework.mainmenu.MainMenuBaseLCUI;
import com.moonsworth.lunar.v1_8.wrapper.client.gui.ScreenInjector;
import com.replaymod.replay.handler.GuiHandler;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiHandler.class)
public class GuiHandlerReplayMixin_v1_8 {

    @ModifyConstant(
            method = "injectIntoIngameMenu",
            constant =  @Constant(stringValue = "replaymod.gui.exit")
    )
    public String ichor$modifyTranslation(String constant) {
        return I18n.format(constant);
    }

    @Inject(
            method = "ensureReplayStopped",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$ensureReplayStopped(GuiScreen guiScreen, CallbackInfo ci) {
        if (!((guiScreen instanceof ScreenInjector && ((ScreenInjector) guiScreen).getCustomScreen() instanceof MainMenuBaseLCUI) || guiScreen instanceof GuiMultiplayer)) {
            ci.cancel();
        }
    }
}
