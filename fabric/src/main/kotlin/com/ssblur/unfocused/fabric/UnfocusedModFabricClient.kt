package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.extension.BlockExtension
import com.ssblur.unfocused.rendering.BlockEntityRendering
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.world.level.block.entity.BlockEntity

@Suppress("UNCHECKED_CAST")
class UnfocusedModFabricClient: ClientModInitializer {
    override fun onInitializeClient() {
        BlockExtension.register{ pair -> BlockRenderLayerMap.INSTANCE.putBlock(pair.first, pair.second) }
        BlockEntityRendering.register{ pair ->
            BlockEntityRenderers.register<BlockEntity>(pair.type, pair.renderer as BlockEntityRendererProvider<BlockEntity>)
        }

        UnfocusedModNetworkingClient.init()

        ClientTickEvents.START_CLIENT_TICK.register{ instance -> instance.level?.let { ClientLevelTickEvent.Before.callback(it) }}
        ClientTickEvents.END_CLIENT_TICK.register{ instance -> instance.level?.let { ClientLevelTickEvent.After.callback(it) }}
    }
}