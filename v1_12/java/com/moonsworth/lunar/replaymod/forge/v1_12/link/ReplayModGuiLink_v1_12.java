package com.moonsworth.lunar.replaymod.forge.v1_12.link;

import com.google.protobuf.Any;
import com.moonsworth.lunar.bridge.client.gui.GuiScreenBridge;
import com.moonsworth.lunar.bridge.client.settings.KeyBindingBridge;
import com.moonsworth.lunar.bridge.util.ResourceLocationBridge;
import com.moonsworth.lunar.client.Lunar;
import com.moonsworth.lunar.client.event.EventBus;
import com.moonsworth.lunar.client.event.impl.gui.EventGuiDrawScreen;
import com.moonsworth.lunar.client.event.impl.internal.EventModUpdate;
import com.moonsworth.lunar.client.external.replaymod.ReplayModLink;
import com.moonsworth.lunar.client.logger.Logger;
import com.moonsworth.lunar.client.management.managers.CosmeticManager;
import com.moonsworth.lunar.client.options.Option;
import com.moonsworth.lunar.client.util.Ref;
import com.replaymod.core.ReplayMod;
import com.replaymod.core.versions.MCVer;
import com.replaymod.editor.gui.MarkerProcessor;
import com.replaymod.lib.de.johni0702.minecraft.gui.utils.EventRegistrations;
import com.replaymod.recording.ReplayModRecording;
import com.replaymod.recording.ServerInfoExt;
import com.replaymod.recording.Setting;
import com.replaymod.recording.gui.GuiRecordingControls;
import com.replaymod.recording.handler.ConnectionEventHandler;
import com.replaymod.recording.packet.PacketListener;
import com.replaymod.render.hooks.EntityRendererHandler;
import com.replaymod.replay.ReplayModReplay;
import com.replaymod.replay.events.ReplayClosedCallback;
import com.replaymod.replay.events.ReplayOpenedCallback;
import com.replaymod.replay.gui.overlay.GuiReplayOverlay;
import com.replaymod.replay.gui.screen.GuiReplayViewer;
import com.replaymod.replaystudio.lib.guava.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static com.replaymod.recording.Setting.INDICATOR;

public class ReplayModGuiLink_v1_12 extends EventRegistrations implements ReplayModLink {

    public static boolean recordingStopped;
    public static boolean recordingPaused;
    public static boolean recordedThisSession;
    public static boolean hasInputOverlayOpen;
    // Not a boolean because I think you can have multiple of these open.
    public static int savingReplayOpen = 0;
    public static Map<String, Option> optionsToRegister = new HashMap<>();
    public static CosmeticInstance cosmeticInstance = null;

    public ReplayModGuiLink_v1_12() {
        EventBus.getBus().register(EventGuiDrawScreen.Pre.class, (e) -> {
            MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.DrawScreenEvent.Pre(
                    (GuiScreen) e.getScreenBridge(), e.getMouseX(), e.getMouseY(), e.getPartialTicks())
            );
        });
        EventBus.getBus().register(EventGuiDrawScreen.Post.class, (e) -> {
            MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.DrawScreenEvent.Post(
                    (GuiScreen) e.getScreenBridge(), e.getMouseX(), e.getMouseY(), e.getPartialTicks())
            );
        });
        EventBus.getBus().register(EventModUpdate.class, (e) -> {
            if (!(e.getMod() instanceof com.moonsworth.lunar.client.feature.mod.replaymod.ReplayMod)) {
                return;
            }
            if (e.isNewState()) {
                if (ReplayModRecording.instance.getConnectionEventHandler() == null || ReplayModRecording.instance.getConnectionEventHandler().getPacketListener() == null) {
                    return;
                }
                boolean autoStart = ReplayMod.instance.getSettingsRegistry().get(Setting.AUTO_START_RECORDING);
                if (Minecraft.getMinecraft().currentServerData != null) {
                    ServerData serverInfo = Minecraft.getMinecraft().currentServerData;

                    Boolean autoStartServer = ServerInfoExt.from(serverInfo).getAutoRecording();
                    if (autoStartServer != null) {
                        autoStart = autoStartServer;
                    }
                } else if (isViewingReplay()) {
                    Logger.info("Cannot start recording in Replay Viewer");
                    return;
                }
                if (!autoStart) {
                    return;
                }
                ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().addMarker(MarkerProcessor.MARKER_NAME_END_CUT);
                ReplayMod.instance.printInfoToChat("replaymod.chat.recordingstarted");
                ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$setStopped(false);
                ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$setPaused(false);
                ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$updateState();
            } else {
                ConnectionEventHandler connectionEventHandler = ReplayModRecording.instance.getConnectionEventHandler();
                if (connectionEventHandler == null || connectionEventHandler.getPacketListener() == null) {
                    return;
                }
                int timestamp = (int) connectionEventHandler.getPacketListener().getCurrentDuration();
                if (!((ConnectionEventHandlerBridge)connectionEventHandler).brige$getGuiControls().isPaused()) {
                    connectionEventHandler.getPacketListener().addMarker(MarkerProcessor.MARKER_NAME_START_CUT, timestamp);
                }
                connectionEventHandler.getPacketListener().addMarker(MarkerProcessor.MARKER_NAME_SPLIT, timestamp + 1);
                ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$setStopped(true);
            }
        });
        on(ReplayOpenedCallback.EVENT, replayHandler -> {
            UUID uuid = Ref.mc().bridge$getSession().bridge$getProfile().getId();
            CosmeticManager manager = Lunar.getClient().getCosmeticManager();
            cosmeticInstance = new CosmeticInstance(
                    new HashSet<>(manager.getItems()),
                    new HashMap<>(manager.getCosmeticEntryMap())
            );
            manager.copyCosmeticData(uuid, HOLDER_UUID);
        });
        on(ReplayClosedCallback.EVENT, replayHandler -> {
            if (cosmeticInstance == null) {
                return;
            }
            UUID uuid = Ref.mc().bridge$getSession().bridge$getProfile().getId();
            CosmeticManager manager = Lunar.getClient().getCosmeticManager();
            manager.getItems().clear();
            manager.getItems().addAll(cosmeticInstance.items());
            manager.getCosmeticEntryMap().clear();
            manager.getCosmeticEntryMap().putAll(cosmeticInstance.entryMap());
            manager.copyCosmeticData(HOLDER_UUID, uuid);
            manager.sync();
        });
        register();
    }

    @Override
    public void handleMainMenuButton() {
        (new GuiReplayViewer(ReplayModReplay.instance)).display();
    }

    @Override
    public boolean isViewingReplay() {
        return ReplayModReplay.instance.getReplayHandler() != null;
    }

    @Override
    public boolean isRecordingStopped() {
        return recordingStopped;
    }

    @Override
    public boolean isRecordingPaused() {
        return recordingPaused;
    }

    @Override
    public boolean isReplayPaused() {
        return ReplayModReplay.instance.getReplayHandler().getReplaySender().paused();
    }

    @Override
    public boolean isRecording() {
        return ReplayModRecording.instance.getConnectionEventHandler().getPacketListener() != null && !isRecordingStopped();
    }

    @Override
    public boolean showRecordingIndicator() {
        return ReplayMod.instance.getSettingsRegistry().get(INDICATOR);
    }

    @Override
    public ResourceLocationBridge getReplayModTexture() {
        return (ResourceLocationBridge) ReplayMod.TEXTURE;
    }

    @Override
    public int getTextureSize() {
        return ReplayMod.TEXTURE_SIZE;
    }

    @Override
    public void handleAssetPacket(Any packet) {
        if (ReplayModRecording.instance.getConnectionEventHandler().getPacketListener() == null) {
            return;
        }

        ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().save(new ReplayModScuffedPacketWrapped(packet));
    }

    private static final Set<Class> HIDE_BRAND_ON = ImmutableSet.of(
            GuiReplayViewer.class,
            GuiReplayOverlay.class
    );

    @Override
    public boolean shouldRenderBrand(GuiScreenBridge guiScreenBridge) {
        if (ReplayModGuiLink_v1_12.hasInputOverlayOpen) {
            return false;
        }
        return !HIDE_BRAND_ON.contains(guiScreenBridge.getClass());
    }

    @Override
    public Set<KeyBindingBridge> getKeyBindings() {
        return (Set<KeyBindingBridge>) (Object) ReplayMod.instance.getKeyBindingRegistry().getBindings().values().stream().map(b -> b.keyBinding).collect(Collectors.toSet());
    }

    /*
     * Resumes a previously paused recording. This method will do nothing if there isn't an existing recording that has been paused.
     */
    @Override
    public void resumeRecording() {
        if (ReplayModRecording.instance.getConnectionEventHandler().getPacketListener() == null) {
            return;
        }
        ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().addMarker(MarkerProcessor.MARKER_NAME_END_CUT);
        ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$setPaused(false);
        ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$updateState();
        Lunar.getClient().getCosmeticManager().sync();
    }


    /*
     * Starts a new recording.
     */
    @Override
    public void startRecording() {
        if (ReplayModRecording.instance.getConnectionEventHandler().getPacketListener() == null) {
            return;
        }
        ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$setPaused(false);
        ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().addMarker(MarkerProcessor.MARKER_NAME_END_CUT);
        ReplayMod.instance.printInfoToChat("replaymod.chat.recordingstarted");
        ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$setStopped(false);
        ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$updateState();
        Lunar.getClient().getCosmeticManager().sync();
    }

    /*
     * Stops an on going recording. This method will do nothing if there isn't a recording occurring.
     */
    @Override
    public void stopRecording() {
        PacketListener packetListener = ReplayModRecording.instance.getConnectionEventHandler().getPacketListener();
        if (packetListener == null) {
            return;
        }
        int timestamp = (int) packetListener.getCurrentDuration();
        GuiRecordingControls guiControls = ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls();
        if (!guiControls.isPaused()) {
            packetListener.addMarker(MarkerProcessor.MARKER_NAME_START_CUT, timestamp);
        }
        packetListener.addMarker(MarkerProcessor.MARKER_NAME_SPLIT, timestamp + 1);
        ((GuiRecordingControlsBridge) guiControls).bridge$setStopped(true);
        ((GuiRecordingControlsBridge) guiControls).bridge$updateState();
    }

    /*
     * Pauses a recording if one is occurring. This method will do nothing if there isn't a recording occurring.
     */
    @Override
    public void pauseRecording() {
        if (ReplayModRecording.instance.getConnectionEventHandler().getPacketListener() == null) {
            return;
        }
        ReplayModRecording.instance.getConnectionEventHandler().getPacketListener().addMarker(MarkerProcessor.MARKER_NAME_START_CUT);
        ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$setPaused(true);
        ((GuiRecordingControlsBridge) ((ConnectionEventHandlerBridge) ReplayModRecording.instance.getConnectionEventHandler()).brige$getGuiControls()).bridge$updateState();
    }

    @Override
    public void noRecover() {
        PacketListener packetListener = ReplayModRecording.instance.getConnectionEventHandler().getPacketListener();
        if (packetListener == null || ((PacketListenerBridge) packetListener).bridge$getOutputPath() == null) {
            return;
        }
        if (recordedThisSession) {
            return;
        }
        try {
            Files.createFile(((PacketListenerBridge) packetListener).bridge$getOutputPath().resolveSibling(((PacketListenerBridge) packetListener).bridge$getOutputPath().getFileName() + ".no_recover"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Map<String, Option> getOptions() {
        return optionsToRegister;
    }

    @Override
    public boolean shouldHideNameTags() {
        EntityRendererHandler handler = ((EntityRendererHandler.IEntityRenderer) MCVer.getMinecraft().entityRenderer).replayModRender_getHandler();
        return handler != null && !handler.getSettings().isRenderNameTags();
    }
}
