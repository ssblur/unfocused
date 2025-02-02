package com.ssblur.unfocused.entity

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import java.util.function.Supplier

@Suppress("unused")
object EntityAttributes: SimpleEvent<EntityAttributes.EntityTypeAndAttributes<out LivingEntity>>(retroactive = true) {
  data class EntityTypeAndAttributes<T: LivingEntity>(
    val type: RegistrySupplier<EntityType<T>>,
    val builder: Supplier<AttributeSupplier.Builder>
  )

  fun <T: LivingEntity> ModInitializer.registerEntityAttributes(
    type: RegistrySupplier<EntityType<T>>,
    builder: Supplier<AttributeSupplier.Builder>
  ) {
    EntityAttributes.callback(EntityTypeAndAttributes(type, builder))
  }
}