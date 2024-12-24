package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.event.common.EntityDamagedEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent


object UnfocusedModEvents {
    fun livingDamageEventBefore(event: LivingDamageEvent.Pre) {
        EntityDamagedEvent.Before.callback(EntityDamagedEvent.EntityDamage(event.entity, event.source, event.originalDamage, EntityDamagedEvent.Before))
        if(EntityDamagedEvent.Before.isCancelled()) event.newDamage = EntityDamagedEvent.Before.value!!
    }

    fun livingDamageEventAfter(event: LivingDamageEvent.Post) {
        EntityDamagedEvent.After.callback(EntityDamagedEvent.EntityDamage(event.entity, event.source, event.originalDamage, EntityDamagedEvent.After))
    }

    fun register() {
        NeoForge.EVENT_BUS.addListener(::livingDamageEventBefore)
        NeoForge.EVENT_BUS.addListener(::livingDamageEventAfter)
    }
}