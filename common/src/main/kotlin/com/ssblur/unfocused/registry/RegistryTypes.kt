package com.ssblur.unfocused.registry

import com.ssblur.unfocused.registry.Registry.Subscriber
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.levelgen.feature.Feature

object RegistryTypes {
    open class RegistryType<T> {
        val registries: ArrayList<Registry<T>> = arrayListOf()
        val subscribers: ArrayList<Subscriber<T>> = arrayListOf()

        fun create(id: String): Registry<T> {
            val registry: Registry<T> = Registry(id)
            registries += registry
            subscribers.forEach { registry.subscribe(it) }
            return registry
        }

        fun subscribe(subscriber: Subscriber<T>) {
            registries.forEach { it.subscribe(subscriber) }
            subscribers += subscriber
        }
    }

    val ITEM: RegistryType<Item> = RegistryType()
    val BLOCK: RegistryType<Block> = RegistryType()
    val EFFECTS: RegistryType<MobEffect> = RegistryType()
    val BLOCK_ENTITIES: RegistryType<BlockEntityType<*>> = RegistryType()
    val DATA_COMPONENTS: RegistryType<DataComponentType<*>> = RegistryType()
    val ENTITIES: RegistryType<EntityType<*>> = RegistryType()
    val FEATURES: RegistryType<Feature<*>> = RegistryType()
}