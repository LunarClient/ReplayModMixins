package com.moonsworth.lunar.replaymod.forge.v1_8.mixin;

import com.lunarclient.gameipc.browser.v1.OpenUrlRequest;
import com.moonsworth.lunar.bridge.BridgeManager;
import com.moonsworth.lunar.client.util.BrowserUtil;
import com.replaymod.core.versions.MCVer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.net.URI;

@Mixin(MCVer.class)
public class MCVerMixin_v1_8 {

    /**
     * Don't use Accessor.
     *
     * @author jado
     */
    @Overwrite
    public static void addButton(GuiScreen screen, GuiButton button) {
        screen.buttonList.add(button);
    }

    /**
     * Use bridge check.
     *
     * @author Tre
     */
    @Overwrite
    public static boolean hasOptifine() {
        return BridgeManager.getOptiFine().isPresent();
    }

    /**
     * Use our own url open'er
     *
     * @author Tre
     */
    @Overwrite
    public static void openURL(URI url) {
        BrowserUtil.openURL(url, OpenUrlRequest.Initiator.INITIATOR_UNSPECIFIED);
    }
}
