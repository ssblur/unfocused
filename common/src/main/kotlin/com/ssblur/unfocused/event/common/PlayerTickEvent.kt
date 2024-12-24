package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.server.level.ServerPlayer

@Suppress("unused")
class PlayerTickEvent: SimpleEvent<ServerPlayer>(false) {
    companion object {
        val Before = PlayerTickEvent()
        val After = PlayerTickEvent()
    }
    // todo hook back
}