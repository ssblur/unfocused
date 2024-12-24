package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.Event
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

@Suppress("unused")
class EntityDamagedEvent(cancelable: Boolean): Event<EntityDamagedEvent.EntityDamage, Float>(false, cancelable) {
    data class EntityDamage(val entity: LivingEntity, val source: DamageSource, val damage: Float, val event: EntityDamagedEvent) {
        fun cancel(value: Float) {
            event.cancel()
            event.cancel(value)
        }
    }
    companion object {
        val Before = EntityDamagedEvent(true)
        val After = EntityDamagedEvent(false)
    }
}