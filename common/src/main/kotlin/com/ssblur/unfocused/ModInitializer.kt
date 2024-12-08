package com.ssblur.unfocused

import com.ssblur.unfocused.registry.RegistryTypes
import net.minecraft.core.component.DataComponentType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.levelgen.feature.Feature
import java.util.function.Consumer
import java.util.function.Supplier

open class ModInitializer(val id: String) {
    var BLOCKS = RegistryTypes.BLOCK.create(id)
    var ITEMS = RegistryTypes.ITEM.create(id)
    var EFFECTS = RegistryTypes.EFFECTS.create(id)
    val BLOCK_ENTITIES = RegistryTypes.BLOCK_ENTITIES.create(id)
    val DATA_COMPONENTS = RegistryTypes.DATA_COMPONENTS.create(id)
    val ENTITIES = RegistryTypes.ENTITIES.create(id)
    val FEATURES = RegistryTypes.FEATURES.create(id)

    fun registerBlock(id: String, supplier: Supplier<Block>): Supplier<Block> {
        return BLOCKS.register(id, supplier)
    }

    fun registerBlockWithItem(id: String, supplier: Supplier<Block>): Pair<Supplier<Block>, Supplier<Item>> {
        val block = BLOCKS.register(id, supplier)
        val item = ITEMS.register(id) { BlockItem(block.get(), Item.Properties()) }
        return Pair(block, item)
    }

    fun registerItem(id: String, supplier: Supplier<Item>): Supplier<Item> {
        return ITEMS.register(id, supplier)
    }

    fun registerEffect(id: String, supplier: Supplier<MobEffect>): Supplier<MobEffect> {
        return EFFECTS.register(id, supplier)
    }

    fun registerBlockEntity(id: String, supplier: Supplier<BlockEntityType<*>>): Supplier<BlockEntityType<*>> {
        return BLOCK_ENTITIES.register(id, supplier)
    }

    fun <T> registerDataComponent(id: String, consumer: Consumer<DataComponentType.Builder<T>>): DataComponentType<T>{
        val builder = DataComponentType.builder<T>()
        consumer.accept(builder)
        val componentType = builder.build()
        DATA_COMPONENTS.register(id) { componentType }
        return componentType
    }

    fun registerEntity(id: String, supplier: Supplier<EntityType<*>>): Supplier<EntityType<*>> {
        return ENTITIES.register(id, supplier)
    }

    fun registerFeature(id: String, supplier: Supplier<Feature<*>>): Supplier<Feature<*>> {
        return FEATURES.register(id, supplier)
    }

    fun location(path: String) = ResourceLocation.fromNamespaceAndPath(id, path)
}