package com.moonsworth.lunar.replaymod.v1_17.mixin;

import com.google.common.collect.Collections2;
import com.moonsworth.lunar.replaymod.v1_17.link.LunarSettingKeys;
import com.replaymod.core.SettingsRegistry;
import com.replaymod.core.events.SettingsChangedCallback;
import com.replaymod.lib.de.johni0702.minecraft.gui.versions.callbacks.InitScreenCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SettingsRegistry.class)
public class SettingsRegistryMixin_v1_17 {

    @Shadow public Map<SettingsRegistry.SettingKey<?>, Object> settings;

    @Inject(
            method = "save",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$save(CallbackInfo ci) {
        var gui = Minecraft.instance.screen;
        if (gui instanceof SelectWorldScreen || gui instanceof JoinMultiplayerScreen) {
            InitScreenCallback.EVENT.invoker().initScreen(gui, Collections2.transform(Collections2.filter(gui.children, it -> it instanceof AbstractButton), it -> (AbstractButton) it));
        }
        ci.cancel();
    }

    @Inject(
            method = "register()V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void ichor$register(CallbackInfo ci) {
        ci.cancel();
    }

    /**
     * @author Tre
     * @reason We don't wanna use their settings.
     */
    @Overwrite
    public void register(SettingsRegistry.SettingKey<?> key) {
        if (!(key instanceof LunarSettingKeys)) {
            throw new IllegalArgumentException("Lunar Setting " + key + " unknown.");
        }
        Object value = ((LunarSettingKeys) key).getOption().get();
        settings.put(key, value);
    }

    /**
     * @author Tre
     */
    @Overwrite
    public <T> T get(SettingsRegistry.SettingKey<T> key) {
        if (!(key instanceof LunarSettingKeys)) {
            throw new IllegalArgumentException("Lunar Setting " + key + " unknown.");
        }
        settings.put(key, ((LunarSettingKeys) key).getOption().get());
        if (!settings.containsKey(key)) {
            throw new IllegalArgumentException("Setting " + key + " unknown.");
        }
        return (T) settings.get(key);
    }

    /**
     * @author Tre
     */
    @Overwrite
    public <T> void set(SettingsRegistry.SettingKey<T> key, T value) {
        if (!(key instanceof LunarSettingKeys)) {
            throw new IllegalArgumentException("Lunar Setting " + key + " unknown.");
        }
        if (key.getDefault() instanceof Boolean) {
            ((LunarSettingKeys) key).getOption().update(value);
        } else if (key.getDefault() instanceof Integer) {
            ((LunarSettingKeys) key).getOption().update(value);
        } else if (key.getDefault() instanceof Double) {
            ((LunarSettingKeys) key).getOption().update(value);
        } else if (key.getDefault() instanceof String) {
            ((LunarSettingKeys) key).getOption().update(value);
        } else {
            throw new IllegalArgumentException("Default type " + key.getDefault().getClass() + " not supported.");
        }
        settings.put(key, value);
        SettingsChangedCallback.EVENT.invoker().onSettingsChanged((SettingsRegistry) (Object) this, key);
    }
}
