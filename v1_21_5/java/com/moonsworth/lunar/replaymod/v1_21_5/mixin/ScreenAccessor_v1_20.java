package com.moonsworth.lunar.replaymod.v1_21_5.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface ScreenAccessor_v1_20 {
    @Invoker("renderMenuBackground")
    void bridge$renderMenuBackground(GuiGraphics partialTick);
}
