package com.ssblur.unfocused.event.server

import com.ssblur.unfocused.event.Event
import net.minecraft.server.level.ServerPlayer

@Suppress("unused")
class PlayerJoinedEvent: Event<ServerPlayer>(false) {
    companion object {
        val Before = PlayerJoinedEvent()
        val After = PlayerJoinedEvent()
    }
    // todo hook back
}