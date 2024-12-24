package com.ssblur.unfocused.event.server

import com.ssblur.unfocused.event.Event
import net.minecraft.server.level.ServerPlayer

@Suppress("unused")
class PlayerTickEvent: Event<ServerPlayer>(false) {
    companion object {
        val Before = PlayerTickEvent()
        val After = PlayerTickEvent()
    }
    // todo hook back
}