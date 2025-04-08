package com.moonsworth.lunar.replaymod.v1_20_2.mixin;

import com.moonsworth.lunar.client.gui.framework.mainmenu.MainMenuBaseLCUI;
import com.moonsworth.lunar.modern.wrapper.client.gui.ScreenInjector;
import com.replaymod.replay.handler.GuiHandler;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(GuiHandler.class)
public class GuiHandlerReplayMixin_v1_20 {

    @Redirect(
            method = "injectIntoIngameMenu",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Iterator;next()Ljava/lang/Object;"
            )
    )
    public Object ichor$modifyOptions(Iterator<AbstractWidget> instance) {
        AbstractWidget abstractWidget = instance.next();
        if (abstractWidget.getMessage().equals(Component.translatable("menu.options"))) {
            abstractWidget.setWidth(98);
        }
        return abstractWidget;
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
