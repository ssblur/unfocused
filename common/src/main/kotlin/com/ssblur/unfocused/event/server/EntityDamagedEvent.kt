package com.ssblur.unfocused.event.server

import com.ssblur.unfocused.event.Event
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

@Suppress("unused")
class EntityDamagedEvent: Event<EntityDamagedEvent.EntityDamage>(false) {
    data class EntityDamage(val entity: LivingEntity, val source: DamageSource, val damage: Float)
    companion object {
        val Before = EntityDamagedEvent()
        val After = EntityDamagedEvent()
    }
    // todo hook back
}