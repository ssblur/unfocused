package com.ssblur.unfocused.mixin;

import com.ssblur.unfocused.event.client.ClientDisconnectEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class AlchimiaeMinecraftMixin {
    @Inject(method = "disconnect()V", at = @At(value = "HEAD"))
    private void alchimiae$clearLevel(CallbackInfo ci) {
        ClientDisconnectEvent.INSTANCE.callback(Minecraft.getInstance().player);
    }
}
