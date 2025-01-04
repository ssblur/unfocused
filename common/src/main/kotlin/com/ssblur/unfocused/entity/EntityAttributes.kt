package com.ssblur.unfocused.entity

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeSupplier

object EntityAttributes: SimpleEvent<Pair<EntityType<out LivingEntity>, AttributeSupplier.Builder>>(retroactive = true) {
    fun ModInitializer.registerEntityAttributes(type: EntityType<out LivingEntity>, builder: AttributeSupplier.Builder) {
        EntityAttributes.callback(Pair(type, builder))
    }
}