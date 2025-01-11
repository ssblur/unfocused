package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.entity.EntityAttributes
import com.ssblur.unfocused.event.common.*
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.loot.LootPool
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.common.NeoForgeMod
import net.neoforged.neoforge.event.LootTableLoadEvent
import net.neoforged.neoforge.event.ServerChatEvent
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent


object UnfocusedModEvents {
    fun livingDamageEventBefore(event: LivingDamageEvent.Pre) {
        EntityDamagedEvent.Before.callback(EntityDamagedEvent.EntityDamage(event.entity, event.source, event.originalDamage, EntityDamagedEvent.Before))
        if(EntityDamagedEvent.Before.isCancelled()) event.newDamage = EntityDamagedEvent.Before.value!!
    }
    fun livingDamageEventAfter(event: LivingDamageEvent.Post) =
        EntityDamagedEvent.After.callback(EntityDamagedEvent.EntityDamage(event.entity, event.source, event.originalDamage, EntityDamagedEvent.After))

    fun chatEvent(event: ServerChatEvent) {
        PlayerChatEvent.Before.callback(PlayerChatEvent.PlayerChatMessage(event.player, event.message, PlayerChatEvent.Before))
        if(PlayerChatEvent.Before.isCancelled()) event.isCanceled = true
        PlayerChatEvent.After.callback(PlayerChatEvent.PlayerChatMessage(event.player, event.message, PlayerChatEvent.After))
    }

    fun playerJoinedEvent(event: PlayerLoggedInEvent) {
        if(!event.entity.level().isClientSide) PlayerJoinedEvent.callback(event.entity as ServerPlayer)
    }

    fun playerTickEventBefore(event: net.neoforged.neoforge.event.tick.PlayerTickEvent.Pre) {
        if(event.entity is ServerPlayer)
            PlayerTickEvent.Before.callback(event.entity as ServerPlayer)
    }
    fun playerTickEventAfter(event: net.neoforged.neoforge.event.tick.PlayerTickEvent.Post) {
        if(event.entity is ServerPlayer)
            PlayerTickEvent.After.callback(event.entity as ServerPlayer)
    }

    fun serverLifecycleEvent(event: ServerStartedEvent) {
        ServerStartEvent.callback(event.server)
    }

    fun attributeEvent(event: EntityAttributeCreationEvent) {
        EntityAttributes.register{ (type, builder) ->
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
        event.register(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS) { register -> // todo fix
            register.register(Unfocused.location("add_feature"), NeoForgeMod.ADD_FEATURES_BIOME_MODIFIER_TYPE.get())
            register.register(Unfocused.location("remove_feature"), NeoForgeMod.REMOVE_FEATURES_BIOME_MODIFIER_TYPE.get())
            register.register(Unfocused.location("add_spawn"), NeoForgeMod.ADD_SPAWNS_BIOME_MODIFIER_TYPE.get())
            register.register(Unfocused.location("remove_spawn"), NeoForgeMod.REMOVE_SPAWNS_BIOME_MODIFIER_TYPE.get())
            register.register(Unfocused.location("add_carver"), NeoForgeMod.ADD_CARVERS_BIOME_MODIFIER_TYPE.get())
            register.register(Unfocused.location("remove_carver"), NeoForgeMod.REMOVE_CARVERS_BIOME_MODIFIER_TYPE.get())
        }
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

        bus.addListener(EventPriority.LOWEST, ::registerEvent)
        bus.addListener(::attributeEvent)
        bus.register(UnfocusedModNetworking)
    }
}