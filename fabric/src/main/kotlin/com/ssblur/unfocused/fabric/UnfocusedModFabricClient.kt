package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.extension.BlockExtension
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap

class UnfocusedModFabricClient: ClientModInitializer {
    override fun onInitializeClient() {
        BlockExtension.register{ pair ->
            BlockRenderLayerMap.INSTANCE.putBlock(pair.first, pair.second)
        }

        UnfocusedModNetworkingClient.init()
    }
}