package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.moonsworth.lunar.client.gui.framework.mainmenu.MainMenuBaseLCUI;
import com.moonsworth.lunar.legacy.wrapper.client.gui.ScreenInjector;
import com.replaymod.replay.handler.GuiHandler;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiHandler.class)
public class GuiHandlerReplayMixin_v1_12 {

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
