package com.moonsworth.lunar.replaymod.v1_12.link;

public interface GuiRecordingControlsBridge {

    void bridge$setStopped(boolean stopped);

    void bridge$setPaused(boolean paused);

    void bridge$updateState();
}
