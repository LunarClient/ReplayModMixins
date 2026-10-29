package com.moonsworth.lunar.replaymod.v26_2.metamixin;

import com.replaymod.core.ReplayModMMLauncher;
import com.replaymod.core.ReplayModNonMMLauncher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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
        if (ichor$hasClass("com.chocohead.mm.Plugin")) {
            // this.logger.debug("Detected MM, they should call us...");
        } else {
            // this.logger.debug("Did not detect MM, initializing ourselves...");
            (new ReplayModMMLauncher()).run();
        }

        return null;
    }

    private static boolean ichor$hasClass(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name.replace('.', '/') + ".class") != null;
    }

}
