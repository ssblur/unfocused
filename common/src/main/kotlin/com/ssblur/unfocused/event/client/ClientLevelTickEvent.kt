package com.ssblur.unfocused.event.client

import com.ssblur.unfocused.event.Event
import net.minecraft.client.multiplayer.ClientLevel

class ClientLevelTickEvent: Event<ClientLevel>(false) {
    companion object {
        val Before = ClientLevelTickEvent()
        val After = ClientLevelTickEvent()
    }
}