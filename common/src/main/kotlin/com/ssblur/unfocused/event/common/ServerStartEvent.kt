package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.server.MinecraftServer

@Suppress("unused")
class ServerStartEvent: SimpleEvent<MinecraftServer>(false) {
    companion object {
        val Before = ServerStartEvent()
        val After = ServerStartEvent()
    }
    // todo hook back
}