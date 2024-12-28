package com.ssblur.unfocused

import com.ssblur.unfocused.registry.RegistrySupplier
import com.ssblur.unfocused.registry.RegistryTypes
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.levelgen.feature.Feature
import java.util.function.Consumer
import java.util.function.Supplier

@Suppress("unused")
open class ModInitializer(val id: String) {
    var BLOCKS = RegistryTypes.BLOCK.create(id)
    var ITEMS = RegistryTypes.ITEM.create(id)
    var EFFECTS = RegistryTypes.EFFECTS.create(id)
    val BLOCK_ENTITIES = RegistryTypes.BLOCK_ENTITIES.create(id)
    val DATA_COMPONENTS = RegistryTypes.DATA_COMPONENTS.create(id)
    val ENTITIES = RegistryTypes.ENTITIES.create(id)
    val FEATURES = RegistryTypes.FEATURES.create(id)
    val RECIPES = RegistryTypes.RECIPES.create(id)
    val RECIPE_TYPES = RegistryTypes.RECIPE_TYPES.create(id)

    fun registerBlock(id: String, supplier: Supplier<Block>): RegistrySupplier<Block> {
        return BLOCKS.register(id, supplier)
    }

    fun registerBlockWithItem(id: String, supplier: Supplier<Block>): Pair<RegistrySupplier<Block>, RegistrySupplier<Item>> {
        val block = BLOCKS.register(id, supplier)
        val item = ITEMS.register(id) { BlockItem(block.get(), Item.Properties()) }
        return Pair(block, item)
    }

    fun registerItem(id: String, supplier: Supplier<Item>): RegistrySupplier<Item> {
        return ITEMS.register(id, supplier)
    }

    fun registerEffect(id: String, supplier: Supplier<MobEffect>): RegistrySupplier<MobEffect> {
        return EFFECTS.register(id, supplier)
    }

    fun registerBlockEntity(id: String, supplier: Supplier<BlockEntityType<*>>): RegistrySupplier<BlockEntityType<*>> {
        return BLOCK_ENTITIES.register(id, supplier)
    }

    fun <T> registerDataComponent(id: String, consumer: Consumer<DataComponentType.Builder<T>>): DataComponentType<T>{
        val builder = DataComponentType.builder<T>()
        consumer.accept(builder)
        val componentType = builder.build()
        DATA_COMPONENTS.register(id) { componentType }
        return componentType
    }

    fun registerEntity(id: String, supplier: Supplier<EntityType<*>>): RegistrySupplier<EntityType<*>> {
        return ENTITIES.register(id, supplier)
    }

    fun registerFeature(id: String, supplier: Supplier<Feature<*>>): RegistrySupplier<Feature<*>> {
        return FEATURES.register(id, supplier)
    }

    fun registerRecipe(id: String, supplier: Supplier<Recipe<*>>): RegistrySupplier<Recipe<*>> {
        return RECIPES.register(id, supplier)
    }

    fun registerRecipeType(id: String, supplier: Supplier<RecipeType<*>>): RegistrySupplier<RecipeType<*>> {
        return RECIPE_TYPES.register(id, supplier)
    }

    fun location(path: String) = ResourceLocation.fromNamespaceAndPath(id, path)

    fun <T> registerTag(registry: ResourceKey<Registry<T>>, path: ResourceLocation) = TagKey.create(registry, path)
    fun <T> registerTag(registry: ResourceKey<Registry<T>>, path: String) = registerTag(registry, location(path))
    fun registerItemTag(path: ResourceLocation) = registerTag(Registries.ITEM, path)
    fun registerItemTag(path: String) = registerItemTag(location(path))
    fun registerBlockTag(path: ResourceLocation) = registerTag(Registries.BLOCK, path)
    fun registerBlockTag(path: String) = registerBlockTag(location(path))
}