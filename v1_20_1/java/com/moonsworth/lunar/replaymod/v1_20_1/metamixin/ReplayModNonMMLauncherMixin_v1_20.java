package com.moonsworth.lunar.replaymod.v1_20_1.metamixin;

import com.replaymod.core.ReplayModMMLauncher;
import com.replaymod.core.ReplayModMixinConfigPlugin;
import com.replaymod.core.ReplayModNonMMLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.IOException;
import java.util.List;

@Mixin(ReplayModNonMMLauncher.class)
public class ReplayModNonMMLauncherMixin_v1_20 {

    /**
     * @reason Nuke the original method's usage of Log4J loggers.
     * @author jado
     * @see com.moonsworth.lunar.replaymod.ichor.ReplayModMixinConfigPluginIchor
     */
    @Overwrite
    public List<String> getMixins() {
        try {
            if (ReplayModMixinConfigPlugin.hasClass("com.chocohead.mm.Plugin")) {
                // this.logger.debug("Detected MM, they should call us...");
            } else {
                // this.logger.debug("Did not detect MM, initializing ourselves...");
                (new ReplayModMMLauncher()).run();
            }

            return null;
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

}
