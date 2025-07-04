package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.biome.BiomeModifiers
import com.ssblur.unfocused.command.CommandRegistration
import com.ssblur.unfocused.entity.EntityAttributes
import com.ssblur.unfocused.entity.Trades
import com.ssblur.unfocused.event.common.*
import com.ssblur.unfocused.neoforge.biome.RegistryAwareBiomeModifier
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction
import net.minecraft.core.NonNullList
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.npc.VillagerTrades
import net.minecraft.world.level.storage.loot.LootPool
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.LootTableLoadEvent
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.event.ServerChatEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent.ItemCraftedEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.event.village.VillagerTradesEvent
import net.neoforged.neoforge.event.village.WandererTradesEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

object UnfocusedModEvents {
  fun livingDamageEventBefore(event: LivingDamageEvent.Pre) {
    EntityDamagedEvent.Before.callback(
      EntityDamagedEvent.EntityDamage(
        event.entity,
        event.source,
        event.originalDamage,
        EntityDamagedEvent.Before
      )
    )
    if (EntityDamagedEvent.Before.isCancelled()) event.newDamage = EntityDamagedEvent.Before.value!!
  }

  fun livingDamageEventAfter(event: LivingDamageEvent.Post) =
    EntityDamagedEvent.After.callback(
      EntityDamagedEvent.EntityDamage(
        event.entity,
        event.source,
        event.originalDamage,
        EntityDamagedEvent.After
      )
    )

  fun chatEvent(event: ServerChatEvent) {
    PlayerChatEvent.Before.callback(
      PlayerChatEvent.PlayerChatMessage(
        event.player,
        event.message,
        PlayerChatEvent.Before
      )
    )
    if (PlayerChatEvent.Before.isCancelled()) event.isCanceled = true
    PlayerChatEvent.After.callback(
      PlayerChatEvent.PlayerChatMessage(
        event.player,
        event.message,
        PlayerChatEvent.After
      )
    )
  }

  fun playerJoinedEvent(event: PlayerLoggedInEvent) {
    if (!event.entity.level().isClientSide) PlayerJoinedEvent.callback(event.entity as ServerPlayer)
  }

  fun playerTickEventBefore(event: net.neoforged.neoforge.event.tick.PlayerTickEvent.Pre) {
    if (event.entity is ServerPlayer)
      PlayerTickEvent.Before.callback(event.entity as ServerPlayer)
  }

  fun playerTickEventAfter(event: net.neoforged.neoforge.event.tick.PlayerTickEvent.Post) {
    if (event.entity is ServerPlayer)
      PlayerTickEvent.After.callback(event.entity as ServerPlayer)
  }

  fun serverLifecycleEvent(event: ServerStartedEvent) {
    ServerStartEvent.callback(event.server)
  }

  fun attributeEvent(event: EntityAttributeCreationEvent) {
    EntityAttributes.register { (type, builder) ->
      event.put(type.get(), builder.get().build())
    }
  }

  fun modifyLootTable(event: LootTableLoadEvent) {
    val pools = mutableListOf<LootPool.Builder>()
    LootTablePopulateEvent.callback(
      LootTablePopulateEvent.LootTableContext(ResourceKey.create(Registries.LOOT_TABLE, event.name), true, pools)
    )
    pools.forEach { event.table.addPool(it.build()) }
  }

  fun registerEvent(event: RegisterEvent) {
    event.register(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS) { register ->
      register.register(Unfocused.location("features"), RegistryAwareBiomeModifier.CODEC)
    }
  }

  fun tradeRegisterEvent(event: VillagerTradesEvent) {
    Trades.register { trade ->
      if (trade.profession == event.type) {
        event.trades.computeIfAbsent(
          trade.rarity,
          Int2ObjectFunction<NonNullList<VillagerTrades.ItemListing>> { NonNullList.create() }
        ).add(trade.trade)
      }
    }
  }

  fun wanderingTradesRegisterEvent(event: WandererTradesEvent) {
    Trades.register { trade ->
      if (trade.rarity > 0)
        event.rareTrades.add(trade.trade)
      else
        event.genericTrades.add(trade.trade)
    }
  }

  fun commandRegistrationEvent(event: RegisterCommandsEvent) {
    CommandRegistration.register {
      it.callback(event.dispatcher, event.buildContext, event.commandSelection)
    }
  }

  fun itemCraftedEvent(event: ItemCraftedEvent) {
    PlayerCraftEvent.callback(PlayerCraftEvent.PlayerCraftData(event.entity, event.crafting, event.inventory))
  }

  fun register(bus: IEventBus) {
    NeoForge.EVENT_BUS.addListener(::livingDamageEventBefore)
    NeoForge.EVENT_BUS.addListener(::livingDamageEventAfter)
    NeoForge.EVENT_BUS.addListener(::chatEvent)
    NeoForge.EVENT_BUS.addListener(::playerJoinedEvent)
    NeoForge.EVENT_BUS.addListener(::playerTickEventBefore)
    NeoForge.EVENT_BUS.addListener(::playerTickEventAfter)
    NeoForge.EVENT_BUS.addListener(::serverLifecycleEvent)
    NeoForge.EVENT_BUS.addListener(::modifyLootTable)
    NeoForge.EVENT_BUS.addListener(::tradeRegisterEvent)
    NeoForge.EVENT_BUS.addListener(::wanderingTradesRegisterEvent)
    NeoForge.EVENT_BUS.addListener(::commandRegistrationEvent)
    NeoForge.EVENT_BUS.addListener(::itemCraftedEvent)

    BiomeModifiers.featureEvent.register{
      println(it.second.feature)
    }

    bus.addListener(EventPriority.LOWEST, ::registerEvent)
    bus.addListener(::attributeEvent)
    bus.register(UnfocusedModNetworking)
  }
}