package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.event.client.ClientLoreEvent
import com.ssblur.unfocused.event.client.ClientScreenRegistrationEvent
import com.ssblur.unfocused.extension.BlockExtension
import com.ssblur.unfocused.rendering.BlockEntityRendering
import com.ssblur.unfocused.rendering.EntityRendering
import com.ssblur.unfocused.rendering.ParticleFactories
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.BlockEntity

@Suppress("unchecked_cast")
class UnfocusedModFabricClient: ClientModInitializer {
  override fun onInitializeClient() {
    BlockExtension.register { pair -> BlockRenderLayerMap.INSTANCE.putBlock(pair.first, pair.second) }
    BlockEntityRendering.register { pair ->
      BlockEntityRenderers.register(pair.type.get(), pair.renderer as BlockEntityRendererProvider<BlockEntity>)
    }
    EntityRendering.register { pair ->
      EntityRendererRegistry.register(pair.type.get(), pair.renderer as EntityRendererProvider<Entity>)
    }
    ParticleFactories.register { pair ->
      pair.ifLeft {
        ParticleFactoryRegistry.getInstance().register(it.particle, it.provider)
      }.ifRight {
        ParticleFactoryRegistry.getInstance().register(it.particle) { sprite ->
          ParticleProvider { options, clientLevel, d, e, f, g, h, i ->
            it.provider(sprite).createParticle(options, clientLevel, d, e, f, g, h, i)
          }
        }
      }
    }

    UnfocusedModNetworkingClient.init()

    ClientTickEvents.START_CLIENT_TICK.register { instance ->
      instance.level?.let {
        ClientLevelTickEvent.Before.callback(
          it
        )
      }
    }
    ClientTickEvents.END_CLIENT_TICK.register { instance -> instance.level?.let { ClientLevelTickEvent.After.callback(it) } }
    ItemTooltipCallback.EVENT.register { stack, context, flag, lore ->
      ClientLoreEvent.callback(ClientLoreEvent.LoreContext(stack, lore, context, flag))
    }
    ClientScreenRegistrationEvent.register{
      MenuScreens.register(it.menu, it.supplier::create as MenuScreens.ScreenConstructor<AbstractContainerMenu, *>)
    }
  }
}