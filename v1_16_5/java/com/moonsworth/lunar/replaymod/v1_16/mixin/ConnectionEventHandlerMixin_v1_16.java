package com.moonsworth.lunar.replaymod.v1_16.mixin;

import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.replaymod.v1_16.link.ReplayModGuiLink_Impl;
import com.replaymod.core.ReplayMod;
import com.replaymod.core.SettingsRegistry;
import com.replaymod.recording.ServerInfoExt;
import com.replaymod.recording.Setting;
import com.replaymod.recording.handler.ConnectionEventHandler;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectionEventHandler.class)
public class ConnectionEventHandlerMixin_v1_16 {

    // Auto start recording
    @Redirect(
            method = "onConnectedToServerEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/SettingsRegistry;get(Lcom/replaymod/core/SettingsRegistry$SettingKey;)Ljava/lang/Object;"
            )
    )
    public <T> Object ichor$get(SettingsRegistry instance, SettingsRegistry.SettingKey<T> key) {
        if (key != Setting.AUTO_START_RECORDING) {
            return instance.get(key);
        }
        if (!Ref.client().getMods().getReplayMod().isEnabled()) {
            return false;
        }
        return instance.get(key);
    }

    @Redirect(
            method = "onConnectedToServerEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/recording/ServerInfoExt;getAutoRecording()Ljava/lang/Boolean;"
            )
    )
    public Boolean ichor$autoRecording(ServerInfoExt instance) {
        if (!Ref.client().getMods().getReplayMod().isEnabled()) {
            return false;
        }
        return instance.getAutoRecording();
    }

    @Redirect(
            method = "onConnectedToServerEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/ReplayMod;isMinimalMode()Z"
            )
    )
    public boolean ichor$isMinimalMode() {
        if (!Ref.client().getMods().getReplayMod().isEnabled()) {
            return false;
        }
        return ReplayMod.isMinimalMode();
    }

    @Inject(
            method = "onConnectedToServerEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/ReplayMod;printInfoToChat(Ljava/lang/String;[Ljava/lang/Object;)V"
            )
    )
    public void ichor$autoRecord(Connection serverInfo, CallbackInfo ci) {
        ReplayModGuiLink_Impl.recordedThisSession = true;
    }

}
