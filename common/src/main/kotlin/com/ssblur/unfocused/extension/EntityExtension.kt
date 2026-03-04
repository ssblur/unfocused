package com.ssblur.unfocused.extension

import net.minecraft.world.entity.Entity

@Suppress("unused")
object EntityExtension {
  infix fun Entity.matches(entity: Entity): Boolean {
    return this.`is`(entity)
  }
}