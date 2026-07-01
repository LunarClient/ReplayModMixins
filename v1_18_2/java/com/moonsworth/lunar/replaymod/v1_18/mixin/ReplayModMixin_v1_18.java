package com.moonsworth.lunar.replaymod.v1_18.mixin;

import com.moonsworth.lunar.client.gui.notification.Anchor;
import com.moonsworth.lunar.client.gui.notification.Popup;
import com.moonsworth.lunar.client.gui.notification.PopupType;
import com.moonsworth.lunar.client.lang.Translatable;
import com.moonsworth.lunar.client.util.Ref;
import com.replaymod.core.ReplayMod;
import com.replaymod.core.Setting;
import com.replaymod.core.SettingsRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReplayMod.class)
public abstract class ReplayModMixin_v1_18 implements Translatable {

    @Shadow
    public abstract SettingsRegistry getSettingsRegistry();

    @Inject(
            method = "printToChat",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$printToChat(boolean warning, String alert, Object[] text, CallbackInfo ci) {
        if (this.getSettingsRegistry().get(Setting.NOTIFICATIONS)) {
            Popup popup = new Popup(warning ? PopupType.WARNING.getIcon() : PopupType.INFO.getIcon(), get("replaymod.title"), get(alert, text));
            popup.setAnchor(Anchor.BOTTOM_RIGHT);
            Ref.client().getPopupManager().add(popup);
            ci.cancel();
        }
    }

    @Override
    public String getLanguagePath() {
        return "replaymod";
    }
}
