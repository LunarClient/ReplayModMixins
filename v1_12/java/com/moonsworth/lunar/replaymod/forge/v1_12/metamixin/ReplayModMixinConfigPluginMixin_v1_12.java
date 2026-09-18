package com.moonsworth.lunar.replaymod.forge.v1_12.metamixin;

import com.replaymod.core.ReplayModMixinConfigPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ReplayModMixinConfigPlugin.class)
public class ReplayModMixinConfigPluginMixin_v1_12 {

    /**
     * @author jado
     * @reason Overwriting to remove the reference to LL's Launch.
     */
    @Overwrite
    public static boolean hasClass(String name) {
        var stream = ReplayModMixinConfigPlugin.class.getClassLoader().getResourceAsStream(name);
        if (stream == null) {
            return false;
        } else {
            try {
                stream.close();
            } catch (Exception ignored) {
            }
            return true;
        }
    }

}
