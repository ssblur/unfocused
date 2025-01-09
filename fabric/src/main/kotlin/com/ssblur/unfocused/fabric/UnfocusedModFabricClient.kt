package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.extension.BlockExtension
import com.ssblur.unfocused.rendering.BlockEntityRendering
import com.ssblur.unfocused.rendering.EntityRendering
import com.ssblur.unfocused.rendering.ParticleFactories
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity

@Suppress("unchecked_cast")
class UnfocusedModFabricClient: ClientModInitializer {
    override fun onInitializeClient() {
        BlockExtension.register{ pair -> BlockRenderLayerMap.INSTANCE.putBlock(pair.first, pair.second) }
        BlockEntityRendering.register{ pair ->
            BlockEntityRenderers.register(pair.type.get(), pair.renderer as BlockEntityRendererProvider<BlockEntity>)
        }
        EntityRendering.register { pair ->
            EntityRendererRegistry.register(pair.type.get(), pair.renderer as EntityRendererProvider<Entity>)
        }
        ParticleFactories.register{ pair ->
            ParticleFactoryRegistry.getInstance().register(pair.particle, pair.provider)
        }

        UnfocusedModNetworkingClient.init()

        ClientTickEvents.START_CLIENT_TICK.register{ instance -> instance.level?.let { ClientLevelTickEvent.Before.callback(it) }}
        ClientTickEvents.END_CLIENT_TICK.register{ instance -> instance.level?.let { ClientLevelTickEvent.After.callback(it) }}
    }
}