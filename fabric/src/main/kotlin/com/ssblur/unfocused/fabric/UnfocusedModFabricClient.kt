package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.extension.BlockExtension
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents

class UnfocusedModFabricClient: ClientModInitializer {
    override fun onInitializeClient() {
        BlockExtension.register{ pair ->
            BlockRenderLayerMap.INSTANCE.putBlock(pair.first, pair.second)
        }

        UnfocusedModNetworkingClient.init()

        ClientTickEvents.START_CLIENT_TICK.register{ instance -> instance.level?.let { ClientLevelTickEvent.Before.callback(it) }}
        ClientTickEvents.END_CLIENT_TICK.register{ instance -> instance.level?.let { ClientLevelTickEvent.After.callback(it) }}

    }
}