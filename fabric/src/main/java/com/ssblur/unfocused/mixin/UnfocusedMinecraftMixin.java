package com.ssblur.unfocused.mixin;

import com.ssblur.unfocused.event.client.ClientDisconnectEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class UnfocusedMinecraftMixin {
    @Inject(method = "clearClientLevel", at = @At(value = "HEAD"))
    private void unfocused$clearLevel(Screen screen, CallbackInfo ci) {
        ClientDisconnectEvent.INSTANCE.callback(Minecraft.getInstance().player);
    }
}
