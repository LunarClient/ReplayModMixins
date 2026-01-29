package com.moonsworth.lunar.replaymod.v1_19.mixin;

import com.moonsworth.lunar.bridge.replaymod.MinecraftGuiScreenBridge;
import com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractGuiScreen.MinecraftGuiScreen.class)
public class MinecraftGuiScreenMixin_v1_19 implements MinecraftGuiScreenBridge {
}

