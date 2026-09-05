package com.moonsworth.lunar.replaymod.v1_21_11.mixin;

import com.moonsworth.lunar.client.feature.FeatureOptions;
import com.moonsworth.lunar.client.feature.FeatureTraits;
import com.moonsworth.lunar.client.options.Option;
import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.client.external.replaymod.LunarSettingKeys;
import com.moonsworth.lunar.replaymod.v1_21_11.link.ReplayModGuiLink_Impl;
import com.replaymod.core.SettingsRegistry;
import org.intellij.lang.annotations.Subst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SettingsRegistry.SettingKeys.class)
public class SettingKeysMixin_v1_20<T> implements LunarSettingKeys {

    @Shadow public T defaultValue;
    @Subst("optionId")
    @Shadow public String key;
    private Option option;
    private boolean wasPreInit = false;

    @Override
    public Option getOption() {
        if (option == null) {
            if (Ref.client() == null || Ref.client().getMods() == null) {
                option = createOption(true);
            } else {
                FeatureOptions featureOptions = Ref.client().getMods().getReplayMod().getOrThrow(FeatureTraits.OPTIONS);
                option = featureOptions.getOptions().stream().filter(option1 -> option1.getId().startsWith(key)).findFirst().orElseGet(() -> {
                    Option option1 = createOption(false);
                    featureOptions.getOptions().add(option1);
                    return option1;
                });
            }
        } else if (wasPreInit) {
            if (Ref.client() != null && Ref.client().getMods() != null) {
                option = ReplayModGuiLink_Impl.optionsToRegister.getOrDefault(option.getId(), option);
                wasPreInit = false;
            }
        }
        return option;
    }

    private Option createOption(boolean preInit) {
        Option option = createUnitializedOption(defaultValue, key);
        if (preInit) {
            wasPreInit = true;
            ReplayModGuiLink_Impl.optionsToRegister.put(option.getId(), option);
        }
        return option;
    }
}
