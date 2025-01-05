package com.ssblur.unfocused.entity

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier

object EntityAttributes: SimpleEvent<Pair<RegistrySupplier<EntityType<out LivingEntity>>, AttributeSupplier.Builder>>(retroactive = true) {
    fun ModInitializer.registerEntityAttributes(type: RegistrySupplier<EntityType<out LivingEntity>>, builder: AttributeSupplier.Builder) {
        EntityAttributes.callback(Pair(type, builder))
    }
}