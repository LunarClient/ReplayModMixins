package com.moonsworth.lunar.replaymod.forge.v1_8.mixin;

import com.moonsworth.lunar.client.external.replaymod.LunarSettingKeys;
import com.moonsworth.lunar.client.feature.FeatureOptions;
import com.moonsworth.lunar.client.feature.FeatureTraits;
import com.moonsworth.lunar.client.options.Option;
import com.moonsworth.lunar.client.util.Ref;
import com.moonsworth.lunar.replaymod.forge.v1_8.link.ReplayModGuiLink_v1_8;
import com.replaymod.core.SettingsRegistry;
import org.intellij.lang.annotations.Subst;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SettingsRegistry.SettingKeys.class)
public class SettingKeysMixin_v1_8<T> implements LunarSettingKeys {

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
                FeatureOptions featureOptions = Ref.client().getMods().getReplayMod().getOrThrow(FeatureTraits.OPTIONS);
                option = featureOptions.getOptions().stream().filter(option1 -> option1.getId().startsWith(key)).findFirst().orElseGet(() -> {
                    Option option1 = createOption(false);
                    featureOptions.getTopOptions().add(option1);
                    return option1;
                });
            }
        } else if (wasPreInit) {
            if (Ref.client() != null && Ref.client().getMods() != null) {
                option = ReplayModGuiLink_v1_8.optionsToRegister.getOrDefault(option.getId(), option);
                wasPreInit = false;
            }
        }
        return option;
    }

    private Option createOption(boolean preInit) {
        Option option = createUnitializedOption(defaultValue, key);
        if (preInit) {
            wasPreInit = true;
            ReplayModGuiLink_v1_8.optionsToRegister.put(option.getId(), option);
        }
        return option;
    }
}
