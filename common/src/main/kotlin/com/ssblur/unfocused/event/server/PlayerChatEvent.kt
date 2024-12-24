package com.ssblur.unfocused.event.server

import com.ssblur.unfocused.event.Event
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer

@Suppress("unused")
class PlayerChatEvent: Event<PlayerChatEvent.PlayerChatMessage>(false) {
    data class PlayerChatMessage(val player: ServerPlayer, val message: Component)
    companion object {
        val Before = PlayerChatEvent()
        val After = PlayerChatEvent()
    }
    // todo hook back
}