package com.moonsworth.lunar.replaymod.v1_17.mixin;

import com.moonsworth.lunar.client.feature.FeatureOptions;
import com.moonsworth.lunar.client.options.Option;
import com.moonsworth.lunar.client.feature.FeatureTraits;
import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.client.external.replaymod.LunarSettingKeys;
import com.moonsworth.lunar.replaymod.v1_17.link.ReplayModGuiLink_Impl;
import com.replaymod.core.SettingsRegistry;
import org.intellij.lang.annotations.Subst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SettingsRegistry.SettingKeys.class)
public class SettingKeysMixin_v1_17<T> implements LunarSettingKeys {

    @Shadow public T defaultValue;
    @Subst("optionId")
    @Shadow public String key;
    private Option lunar$option;
    private boolean lunar$wasPreInit = false;

    @Override
    public Option getOption() {
        if (lunar$option == null) {
            if (Ref.client() == null || Ref.client().getMods() == null) {
                lunar$option = lunar$createOption(true);
            } else {
                FeatureOptions featureOptions = Ref.client().getMods().getReplayMod().getOrThrow(FeatureTraits.OPTIONS);
                lunar$option = featureOptions.getOptions().stream().filter(option1 -> option1.getId().startsWith(key)).findFirst().orElseGet(() -> {
                    Option option1 = lunar$createOption(false);
                    featureOptions.getTopOptions().add(option1);
                    return option1;
                });
            }
        } else if (lunar$wasPreInit) {
            if (Ref.client() != null && Ref.client().getMods() != null) {
                lunar$option = ReplayModGuiLink_Impl.getOptionsToRegister()
                        .getOrDefault(lunar$option.getId(), lunar$option);
                lunar$wasPreInit = false;
            }
        }
        return lunar$option;
    }

    private Option lunar$createOption(boolean preInit) {
        Option option = createUnitializedOption(defaultValue, key);
        if (preInit) {
            lunar$wasPreInit = true;
            ReplayModGuiLink_Impl.getOptionsToRegister().put(option.getId(), option);
        }
        return option;
    }
}
