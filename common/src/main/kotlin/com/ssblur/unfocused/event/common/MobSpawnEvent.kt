package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.Event
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

@Suppress("unused")
object MobSpawnEvent: Event<MobSpawnEvent.EntitySpawn, Boolean>(false, true) {
  data class EntitySpawn(
    val entity: Entity,
    val level: Level,
  ) {
    fun cancel(value: Boolean) {
      cancel(value)
    }
  }
}