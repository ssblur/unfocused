package com.ssblur.unfocused.rendering

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

@Suppress("unused")
object EntityRendering: SimpleEvent<EntityRendering.EntityAndRenderer<out Entity>>(retroactive = true) {
    data class EntityAndRenderer<T: Entity>(val type: RegistrySupplier<EntityType<T>>, val renderer: EntityRendererProvider<T>)
    fun <T: Entity> ModInitializer.registerEntityRenderer(type: RegistrySupplier<EntityType<T>>, renderer: EntityRendererProvider<T>) {
        EntityRendering.callback(EntityAndRenderer(type, renderer))
    }
}