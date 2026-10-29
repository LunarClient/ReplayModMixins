package com.moonsworth.lunar.replaymod.v1_21_9.mixin;

import com.moonsworth.lunar.bridge.replaymod.MinecraftGuiScreenBridge;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiScreen$MinecraftGuiScreen")
public class MinecraftGuiScreenMixin_v1_20 implements MinecraftGuiScreenBridge {
}
