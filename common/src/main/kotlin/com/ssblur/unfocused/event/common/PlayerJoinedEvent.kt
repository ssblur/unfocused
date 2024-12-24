package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.server.level.ServerPlayer

@Suppress("unused")
class PlayerJoinedEvent: SimpleEvent<ServerPlayer>(false) {
    companion object {
        val Before = PlayerJoinedEvent()
        val After = PlayerJoinedEvent()
    }
    // todo hook back
}