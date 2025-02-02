package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

@Suppress("unused")
class PlayerChatEvent: SimpleEvent<PlayerChatEvent.PlayerChatMessage>(false, true) {
  data class PlayerChatMessage(val player: ServerPlayer, val message: Component, val event: PlayerChatEvent) {
    fun cancel() {
      event.cancel()
    }
  }

  companion object {
    val Before = PlayerChatEvent()
    val After = PlayerChatEvent()
  }
}