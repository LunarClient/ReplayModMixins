package com.moonsworth.lunar.replaymod.v1_17.mixin;

import com.moonsworth.lunar.client.gui.framework.mainmenu.MainMenuBaseLCUI;
import com.moonsworth.lunar.modern.wrapper.client.gui.ScreenInjector;
import com.replaymod.replay.handler.GuiHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiHandler.class)
public class GuiHandlerReplayMixin_v1_17 {

    @ModifyConstant(
            method = "injectIntoIngameMenu",
            constant = @Constant(intValue = 204)
    )
    public int ichor$modifyOptions(int constant) {
        return 98;
    }

    @ModifyConstant(
            method = "injectIntoIngameMenu",
            constant =  @Constant(stringValue = "replaymod.gui.exit")
    )
    public String ichor$modifyTranslation(String constant) {
        return I18n.get(constant);
    }

    @Inject(
            method = "ensureReplayStopped",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$ensureReplayStopped(Screen guiScreen, CallbackInfo ci) {
        if (!((guiScreen instanceof ScreenInjector && ((ScreenInjector) guiScreen).getCustomScreen() instanceof MainMenuBaseLCUI) || guiScreen instanceof JoinMultiplayerScreen)) {
            ci.cancel();
        }
    }
}
