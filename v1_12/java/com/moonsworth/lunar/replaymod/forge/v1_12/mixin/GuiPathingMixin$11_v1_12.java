package com.moonsworth.lunar.replaymod.forge.v1_12.mixin;

import com.replaymod.pathing.player.RealtimeTimelinePlayer;
import com.replaymod.simplepathing.gui.GuiPathing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

@Mixin(targets = "com.replaymod.simplepathing.gui.GuiPathing$10")
public class GuiPathingMixin$11_v1_12 {

    @Shadow
    public GuiPathing this$0;

    @Inject(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/util/concurrent/ListenableFuture;cancel(Z)Z",
                    shift = At.Shift.AFTER
            )
    )
    public void ichor$tickPlayer(CallbackInfo ci) throws NoSuchFieldException, IllegalAccessException {
        // Hopefully someone will add PublicAccess to legacy replaymod
        Class<?> clazz = this.this$0.getClass();
        Field playerField = clazz.getDeclaredField("player");
        playerField.setAccessible(true);

        // Tear down of the player might only happen the next tick after it was cancelled
        RealtimeTimelinePlayer player = (RealtimeTimelinePlayer) playerField.get(this.this$0);
        player.onTick();
    }
}
