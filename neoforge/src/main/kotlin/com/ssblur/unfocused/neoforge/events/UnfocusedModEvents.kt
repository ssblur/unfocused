package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.event.common.EntityDamagedEvent
import com.ssblur.unfocused.event.common.PlayerChatEvent
import com.ssblur.unfocused.event.common.PlayerJoinedEvent
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.ServerChatEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent


object UnfocusedModEvents {
    fun livingDamageEventBefore(event: LivingDamageEvent.Pre) {
        EntityDamagedEvent.Before.callback(EntityDamagedEvent.EntityDamage(event.entity, event.source, event.originalDamage, EntityDamagedEvent.Before))
        if(EntityDamagedEvent.Before.isCancelled()) event.newDamage = EntityDamagedEvent.Before.value!!
    }

    fun livingDamageEventAfter(event: LivingDamageEvent.Post) {
        EntityDamagedEvent.After.callback(EntityDamagedEvent.EntityDamage(event.entity, event.source, event.originalDamage, EntityDamagedEvent.After))
    }

    fun chatEvent(event: ServerChatEvent) {
        PlayerChatEvent.Before.callback(PlayerChatEvent.PlayerChatMessage(event.player, event.message, PlayerChatEvent.Before))
        if(PlayerChatEvent.Before.isCancelled()) event.isCanceled = true
        PlayerChatEvent.After.callback(PlayerChatEvent.PlayerChatMessage(event.player, event.message, PlayerChatEvent.After))
    }

    fun playerJoinedEvent(event: PlayerLoggedInEvent) {
        if(!event.entity.level().isClientSide) PlayerJoinedEvent.callback(event.entity as ServerPlayer)
    }

    fun register() {
        NeoForge.EVENT_BUS.addListener(::livingDamageEventBefore)
        NeoForge.EVENT_BUS.addListener(::livingDamageEventAfter)
        NeoForge.EVENT_BUS.addListener(::chatEvent)
        NeoForge.EVENT_BUS.addListener(::playerJoinedEvent)
    }
}