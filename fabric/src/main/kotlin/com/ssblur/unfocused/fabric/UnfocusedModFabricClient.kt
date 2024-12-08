package com.ssblur.unfocused.fabric

import net.fabricmc.api.ClientModInitializer

class UnfocusedModFabricClient: ClientModInitializer {
    override fun onInitializeClient() {
        UnfocusedModNetworkingClient.init()
    }
}