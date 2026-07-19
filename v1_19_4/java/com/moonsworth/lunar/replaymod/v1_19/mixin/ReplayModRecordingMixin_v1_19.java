package com.moonsworth.lunar.replaymod.v1_19.mixin;

import com.moonsworth.lunar.client.external.ExternalLinks;
import com.moonsworth.lunar.client.external.replaymod.ReplayModLink;
import com.replaymod.core.KeyBindingRegistry;
import com.replaymod.core.ReplayMod;
import com.replaymod.recording.ReplayModRecording;
import com.replaymod.recording.packet.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ReplayModRecording.class)
public abstract class ReplayModRecordingMixin_v1_19 {

    /**
     * @author Tre - Was too lazy to inject into runnable.
     */
    @Overwrite
    public void registerKeyBindings(KeyBindingRegistry registry) {
        registry.registerKeyBinding("replaymod.input.marker", 77, () -> {
            PacketListener packetListener = ReplayModRecording.instance.connectionEventHandler.getPacketListener();
            if (packetListener != null && ExternalLinks.get(ReplayModLink.class).map(ReplayModLink::isRecording).orElse(true)) {
                packetListener.addMarker(null);
                ReplayMod.instance.printInfoToChat("replaymod.chat.addedmarker");
            }

        }, false);
    }
}
