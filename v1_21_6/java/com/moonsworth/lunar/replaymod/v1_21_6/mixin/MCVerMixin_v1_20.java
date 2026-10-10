package com.moonsworth.lunar.replaymod.v1_21_6.mixin;

import com.lunarclient.gameipc.browser.v1.OpenUrlRequest;
import com.moonsworth.lunar.bridge.BridgeManager;
import com.moonsworth.lunar.client.util.BrowserUtil;
import com.replaymod.core.versions.MCVer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.net.URI;

@Mixin(MCVer.class)
public class MCVerMixin_v1_20 {

    /**
     * @reason Use bridge check.
     *
     * @author Tre
     */
    @Overwrite
    public static boolean hasOptifine() {
        return BridgeManager.getOptiFine().isPresent();
    }

    /**
     * @reason Use our own url open'er
     *
     * @author Tre
     */
    @Overwrite
    public static void openURL(URI url) {
        BrowserUtil.openURL(url, OpenUrlRequest.Initiator.INITIATOR_UNSPECIFIED);
    }
}
