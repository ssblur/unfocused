package com.ssblur.unfocused.event.server

import com.ssblur.unfocused.event.Event
import net.minecraft.server.MinecraftServer

@Suppress("unused")
class ServerStartEvent: Event<MinecraftServer>(false) {
    companion object {
        val Before = ServerStartEvent()
        val After = ServerStartEvent()
    }
    // todo hook back
}