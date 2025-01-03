package com.ssblur.unfocused.registry

import com.ssblur.unfocused.registry.Registry.Subscriber
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.levelgen.feature.Feature

object RegistryTypes {
    open class RegistryType<T>(val key: ResourceKey<net.minecraft.core.Registry<T>>) {
        private val registries: ArrayList<Registry<T>> = arrayListOf()
        private val subscribers: ArrayList<Subscriber<T>> = arrayListOf()

        fun create(id: String): Registry<T> {
            val registry: Registry<T> = Registry(id, key)
            registries += registry
            subscribers.forEach { registry.subscribe(it) }
            return registry
        }

        fun subscribe(subscriber: Subscriber<T>) {
            registries.forEach { it.subscribe(subscriber) }
            subscribers += subscriber
        }
    }

    val ITEM: RegistryType<Item> = RegistryType(Registries.ITEM)
    val BLOCK: RegistryType<Block> = RegistryType(Registries.BLOCK)
    val EFFECTS: RegistryType<MobEffect> = RegistryType(Registries.MOB_EFFECT)
    val BLOCK_ENTITIES: RegistryType<BlockEntityType<*>> = RegistryType(Registries.BLOCK_ENTITY_TYPE)
    val DATA_COMPONENTS: RegistryType<DataComponentType<*>> = RegistryType(Registries.DATA_COMPONENT_TYPE)
    val ENTITIES: RegistryType<EntityType<*>> = RegistryType(Registries.ENTITY_TYPE)
    val FEATURES: RegistryType<Feature<*>> = RegistryType(Registries.FEATURE)
    val RECIPE_TYPES: RegistryType<RecipeType<*>> = RegistryType(Registries.RECIPE_TYPE)
    val RECIPES: RegistryType<Recipe<*>> = RegistryType(Registries.RECIPE)
}