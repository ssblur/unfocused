package com.ssblur.unfocused.mixin;

import com.ssblur.unfocused.event.common.PlayerTickEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class PlayerMixin {
  @Inject(method = "tick", at = @At("HEAD"))
  private void unfocused$tick$pre(CallbackInfo ci) {
    Player self = (ServerPlayer) (Object) this;
    PlayerTickEvent.Companion.getBefore().callback(self);
  }

  @Inject(method = "tick", at = @At("TAIL"))
  private void unfocused$tick$post(CallbackInfo ci) {
    Player self = (ServerPlayer) (Object) this;
    PlayerTickEvent.Companion.getAfter().callback(self);
  }
}
