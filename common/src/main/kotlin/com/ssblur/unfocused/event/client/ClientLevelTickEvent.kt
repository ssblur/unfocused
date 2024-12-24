package com.ssblur.unfocused.event.client

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.client.multiplayer.ClientLevel

class ClientLevelTickEvent: SimpleEvent<ClientLevel>(false) {
    companion object {
        val Before = ClientLevelTickEvent()
        val After = ClientLevelTickEvent()
    }
}