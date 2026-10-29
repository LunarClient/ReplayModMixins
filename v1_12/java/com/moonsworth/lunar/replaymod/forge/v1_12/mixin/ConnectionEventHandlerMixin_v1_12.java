package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.replaymod.forge.v1_12.link.ConnectionEventHandlerBridge;
import com.moonsworth.lunar.replaymod.forge.v1_12.link.ReplayModGuiLink_v1_12;
import com.replaymod.core.SettingsRegistry;
import com.replaymod.recording.ServerInfoExt;
import com.replaymod.recording.Setting;
import com.replaymod.recording.gui.GuiRecordingControls;
import com.replaymod.recording.handler.ConnectionEventHandler;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectionEventHandler.class)
public class ConnectionEventHandlerMixin_v1_12 implements ConnectionEventHandlerBridge {

    @Shadow private GuiRecordingControls guiControls;

    // Auto start recording
    @WrapOperation(
            method = "onConnectedToServerEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/SettingsRegistry;get(Lcom/replaymod/core/SettingsRegistry$SettingKey;)Ljava/lang/Object;"
            )
    )
    public Object ichor$get(SettingsRegistry instance, SettingsRegistry.SettingKey<?> key, Operation<Object> original) {
        if (key == Setting.AUTO_START_RECORDING && !Ref.client().getMods().getReplayMod().isEnabled()) {
            return false;
        }
        return original.call(instance, key);
    }

    @WrapOperation(
            method = "onConnectedToServerEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/recording/ServerInfoExt;getAutoRecording()Ljava/lang/Boolean;"
            )
    )
    public Boolean ichor$autoRecording(ServerInfoExt instance, Operation<Boolean> original) {
        if (!Ref.client().getMods().getReplayMod().isEnabled()) {
            return false;
        }
        return original.call(instance);
    }

    @WrapOperation(
            method = "onConnectedToServerEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/ReplayMod;isMinimalMode()Z"
            )
    )
    public boolean ichor$isMinimalMode(Operation<Boolean> original) {
        if (!Ref.client().getMods().getReplayMod().isEnabled()) {
            return false;
        }
        return original.call();
    }

    @Inject(
            method = "onConnectedToServerEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/replaymod/core/ReplayMod;printInfoToChat(Ljava/lang/String;[Ljava/lang/Object;)V"
            )
    )
    public void ichor$autoRecord(NetworkManager serverInfo, CallbackInfo ci) {
        ReplayModGuiLink_v1_12.setRecordedThisSession(true);
    }

    @Override
    public GuiRecordingControls brige$getGuiControls() {
        return guiControls;
    }
}
