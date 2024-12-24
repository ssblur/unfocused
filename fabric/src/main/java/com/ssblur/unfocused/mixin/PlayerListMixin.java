package com.ssblur.unfocused.mixin;

import com.ssblur.unfocused.event.common.PlayerJoinedEvent;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {
  @Inject(method = "placeNewPlayer", at = @At("RETURN"))
  private void unfocused$placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
    PlayerJoinedEvent.INSTANCE.callback(serverPlayer);
  }
}
