package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.feature.FeatureOptions;
import com.moonsworth.lunar.client.gui.framework.components.TextOption;
import com.moonsworth.lunar.client.options.BooleanOption;
import com.moonsworth.lunar.client.options.NumberOption;
import com.moonsworth.lunar.client.options.Option;
import com.moonsworth.lunar.client.trait.Traits;
import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.replaymod.forge.v1_12.link.LunarSettingKeys;
import com.moonsworth.lunar.replaymod.forge.v1_12.link.ReplayModGuiLink_v1_12;
import com.replaymod.core.SettingsRegistry;
import org.intellij.lang.annotations.Subst;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SettingsRegistry.SettingKeys.class)
public class SettingKeysMixin_v1_12<T> implements LunarSettingKeys {

    @Final
    @Shadow private T defaultValue;
    @Subst("optionId")
    @Final
    @Shadow private String key;
    private Option option;
    private boolean wasPreInit = false;

    @Override
    public Option getOption() {
        if (option == null) {
            if (Ref.client() == null || Ref.client().getMods() == null) {
                option = createOption(true);
            } else {
                FeatureOptions featureOptions = Ref.client().getMods().getReplayMod().getOrThrow(Traits.FEATURE_OPTIONS);
                option = featureOptions.getOptions().stream().filter(option1 -> option1.getId().startsWith(key)).findFirst().orElseGet(() -> {
                    Option option1 = createOption(false);
                    featureOptions.getTopOptions().add(option1);
                    return option1;
                });
            }
        } else if (wasPreInit) {
            if (Ref.client() != null && Ref.client().getMods() != null) {
                option = ReplayModGuiLink_v1_12.optionsToRegister.getOrDefault(option.getId(), option);
                wasPreInit = false;
            }
        }
        return option;
    }

    private Option createOption(boolean preInit) {
        Option option;
        if (defaultValue instanceof Boolean) {
            option = new BooleanOption(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Integer) {
            option = new NumberOption<>(key, (Integer) defaultValue, 1700, 1900);
        } else if (defaultValue instanceof Double) {
            option = new NumberOption<>(key, (Double) defaultValue, 0D, 100D);
        } else if (defaultValue instanceof String) {
            option = new TextOption(key, (String) defaultValue);
        } else {
            throw new IllegalArgumentException("Default type " + defaultValue.getClass() + " not supported.");
        }
        if (preInit) {
            wasPreInit = true;
            ReplayModGuiLink_v1_12.optionsToRegister.put(option.getId(), option);
        }
        return option;
    }
}
