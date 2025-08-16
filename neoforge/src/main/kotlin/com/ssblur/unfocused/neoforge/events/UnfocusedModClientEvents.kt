package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.event.client.*
import com.ssblur.unfocused.neoforge.UtilityExpectPlatformClientImpl
import com.ssblur.unfocused.rendering.BlockEntityRendering
import com.ssblur.unfocused.rendering.EntityRendering
import com.ssblur.unfocused.rendering.ParticleFactories
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import org.joml.Vector2d

@Suppress("unchecked_cast", "unused", "unused_parameter")
object UnfocusedModClientEvents {
  fun clientTickEventBefore(event: ClientTickEvent.Pre) =
    Minecraft.getInstance().level?.let(ClientLevelTickEvent.Before::callback)

  fun clientTickEventAfter(event: ClientTickEvent.Post) =
    Minecraft.getInstance().level?.let(ClientLevelTickEvent.After::callback)

  fun clientScrollEvent(event: InputEvent.MouseScrollingEvent) {
    MouseScrollEvent.callback(
      MouseScrollEvent.KeyPress(
        Minecraft.getInstance(),
        Vector2d(event.scrollDeltaX, event.scrollDeltaY),
        MouseScrollEvent
      )
    )
    if (MouseScrollEvent.isCancelled()) event.isCanceled = true
  }

  fun registerEntityRendererEvent(event: EntityRenderersEvent.RegisterRenderers) {
    BlockEntityRendering.register { pair ->
      event.registerBlockEntityRenderer(pair.type.get(), pair.renderer as BlockEntityRendererProvider<BlockEntity>)
    }
    EntityRendering.register { pair ->
      event.registerEntityRenderer(pair.type.get(), pair.renderer as EntityRendererProvider<Entity>)
    }
  }

  fun registerParticleProviders(event: RegisterParticleProvidersEvent) {
    ParticleFactories.register { pair ->
      pair.ifLeft {
        event.registerSpecial(it.particle, it.provider)
      }.ifRight {
        event.registerSpriteSet(it.particle) { sprite ->
          ParticleProvider { options, clientLevel, d, e, f, g, h, i ->
            it.provider(sprite).createParticle(options, clientLevel, d, e, f, g, h, i)
          }
        }
      }
    }
  }

  fun clientLoreEvent(event: ItemTooltipEvent) {
    ClientLoreEvent.callback(ClientLoreEvent.LoreContext(event.itemStack, event.toolTip, event.context, event.flags))
  }

  fun blockColorEvent(event: RegisterColorHandlersEvent.Block) {
    UtilityExpectPlatformClientImpl.BLOCK_COLORS.forEach { (color, supplier) ->
      event.register(color, supplier.get())
    }
  }

//  fun itemColorEvent(event: RegisterColorHandlersEvent.Item) {
//    UtilityExpectPlatformImpl.ITEM_COLORS.forEach { (color, supplier) ->
//      event.register(color, supplier.get())
//    }
//  }

  fun registerScreenEvent(event: RegisterMenuScreensEvent) {
    ClientScreenRegistrationEvent.register{
      event.register(it.menu, it.supplier)
    }
  }

  fun logoutEvent(event: ClientPlayerNetworkEvent.LoggingOut) {
    ClientDisconnectEvent.callback(event.player)
  }

  fun register(bus: IEventBus) {
    NeoForge.EVENT_BUS.addListener(::clientTickEventAfter)
    NeoForge.EVENT_BUS.addListener(::clientTickEventBefore)
    NeoForge.EVENT_BUS.addListener(::clientScrollEvent)
    NeoForge.EVENT_BUS.addListener(::clientLoreEvent)
    NeoForge.EVENT_BUS.addListener(::logoutEvent)

//    bus.addListener(::itemColorEvent)
    bus.addListener(::blockColorEvent)
    bus.addListener(::registerEntityRendererEvent)
    bus.addListener(::registerScreenEvent)
    bus.addListener(::registerParticleProviders)
  }
}