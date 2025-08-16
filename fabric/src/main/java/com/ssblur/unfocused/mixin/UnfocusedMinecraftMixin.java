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
    @SuppressWarnings("unchecked")
    @Inject(method = "disconnect", at = @At(value = "HEAD"))
    private void alchimiae$disconnect(Screen screen, boolean bl, CallbackInfo ci) {
        ClientDisconnectEvent.INSTANCE.callback(Minecraft.getInstance().player);
    }
}
