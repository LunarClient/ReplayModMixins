package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.moonsworth.lunar.bridge.BridgeManager;
import com.replaymod.compat.optifine.DisableFastRender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DisableFastRender.class)
public class DisableFastRenderMixin_v1_12 {
    @Shadow
    private boolean wasFastRender;

    /**
     * @author Gecko
     * @reason replace reflection with bridge
     */
    @Overwrite
    public void onRenderBegin() {
        BridgeManager.getOptiFine().ifPresent(optifine -> {
            wasFastRender = optifine.getConfig().hasFastRender();
            optifine.getConfig().setFastRender(false);
        });
    }

    /**
     * @author Gecko
     * @reason replace reflection with bridge
     */
    @Overwrite
    public void onRenderEnd() {
        BridgeManager.getOptiFine().ifPresent(optifine -> {
            optifine.getConfig().setFastRender(wasFastRender);
        });
    }
}
