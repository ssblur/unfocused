package com.ssblur.unfocused

import com.ssblur.unfocused.advancement.GenericTrigger
import com.ssblur.unfocused.config.Config
import com.ssblur.unfocused.config.GameRuleConfig
import com.ssblur.unfocused.registry.RegistrySupplier
import com.ssblur.unfocused.registry.RegistryTypes
import com.ssblur.unfocused.serialization.KClassCodec
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.KClass

@Suppress("unused", "unchecked_cast")
open class ModInitializer(val id: String) {
  val CONFIG: Config by lazy { Config(id) }
  val config: GameRuleConfig by lazy { GameRuleConfig(this) }

  var BLOCKS = RegistryTypes.BLOCK.create(id)
  var ITEMS = RegistryTypes.ITEM.create(id)
  var EFFECTS = RegistryTypes.EFFECTS.create(id)
  val BLOCK_ENTITIES = RegistryTypes.BLOCK_ENTITIES.create(id)
  val DATA_COMPONENTS = RegistryTypes.DATA_COMPONENTS.create(id)
  val ENTITIES = RegistryTypes.ENTITIES.create(id)
  val FEATURES = RegistryTypes.FEATURES.create(id)
  val RECIPES = RegistryTypes.RECIPES.create(id)
  val RECIPE_TYPES = RegistryTypes.RECIPE_TYPES.create(id)
  val LOOT_FUNCTION_TYPES = RegistryTypes.LOOT_FUNCTION_TYPES.create(id)
  val LOOT_CONDITION_TYPES = RegistryTypes.LOOT_CONDITION_TYPES.create(id)
  val TRIGGER_TYPES = RegistryTypes.TRIGGER_TYPES.create(id)
  val RECIPE_SERIALIZERS = RegistryTypes.RECIPE_SERIALIZERS.create(id)
  val CREATIVE_TABS = RegistryTypes.CREATIVE_TABS.create(id)
  val PARTICLE_TYPES = RegistryTypes.PARTICLE_TYPES.create(id)
  val MENUS = RegistryTypes.MENUS.create(id)
  val ARMOR = RegistryTypes.ARMOR.create(id)
  val SOUNDS = RegistryTypes.SOUNDS.create(id)

  fun registerBlock(id: String, supplier: Supplier<Block>): RegistrySupplier<Block> {
    return BLOCKS.register(id, supplier)
  }

  fun registerBlockWithItem(
    id: String,
    supplier: Supplier<Block>
  ): Pair<RegistrySupplier<Block>, RegistrySupplier<Item>> {
    val block = BLOCKS.register(id, supplier)
    val item = ITEMS.register(id) { BlockItem(block.get(), Item.Properties()) }
    return Pair(block, item)
  }

  fun registerItem(id: String, supplier: Supplier<Item>): RegistrySupplier<Item> {
    return ITEMS.register(id, supplier)
  }

  fun registerArmorMaterial(id: String, supplier: Supplier<ArmorMaterial>): RegistrySupplier<ArmorMaterial> {
    return ARMOR.register(id, supplier)
  }

  fun registerEffect(id: String, supplier: Supplier<MobEffect>): RegistrySupplier<MobEffect> {
    return EFFECTS.register(id, supplier)
  }

  fun <T: BlockEntity> registerBlockEntity(
    id: String,
    supplier: Supplier<BlockEntityType<T>>
  ): RegistrySupplier<BlockEntityType<T>> {
    return BLOCK_ENTITIES.register(id, supplier as Supplier<BlockEntityType<*>>) as RegistrySupplier<BlockEntityType<T>>
  }

  fun <T> registerDataComponent(id: String, consumer: Consumer<DataComponentType.Builder<T>>): DataComponentType<T> {
    val builder = DataComponentType.builder<T>()
    consumer.accept(builder)
    val componentType = builder.build()
    DATA_COMPONENTS.register(id) { componentType }
    return componentType
  }

  fun <T: Any> registerDataComponent(id: String, type: KClass<T>): DataComponentType<T> {
    val builder = DataComponentType.builder<T>()
    val componentType = builder.networkSynchronized(KClassCodec.streamCodec(type)).persistent(KClassCodec.codec(type)).build()
    DATA_COMPONENTS.register(id) { componentType }
    return componentType
  }

  fun <T: Entity> registerEntity(id: String, supplier: Supplier<EntityType<T?>>): RegistrySupplier<EntityType<T>> {
    return ENTITIES.register(id, supplier as Supplier<EntityType<*>>) as RegistrySupplier<EntityType<T>>
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

  fun <T: LootItemFunction> registerLootFunction(
    id: String,
    supplier: Supplier<LootItemFunctionType<T>>
  ): RegistrySupplier<LootItemFunctionType<T>> {
    return LOOT_FUNCTION_TYPES.register(
      id,
      supplier as Supplier<LootItemFunctionType<*>>
    ) as RegistrySupplier<LootItemFunctionType<T>>
  }

  fun registerLootCondition(
    id: String,
    supplier: Supplier<LootItemConditionType>
  ): RegistrySupplier<LootItemConditionType> {
    return LOOT_CONDITION_TYPES.register(id, supplier)
  }

  fun <T: CriterionTrigger<*>> registerTrigger(id: String, supplier: Supplier<T>): RegistrySupplier<T> {
    return TRIGGER_TYPES.register(id, supplier as Supplier<CriterionTrigger<*>>) as RegistrySupplier<T>
  }

  fun registerGenericTrigger(id: String): RegistrySupplier<GenericTrigger> {
    return TRIGGER_TYPES.register(id) { GenericTrigger(location(id)) } as RegistrySupplier<GenericTrigger>
  }

  fun <T: Recipe<*>> registerRecipeSerializer(
    id: String,
    supplier: Supplier<RecipeSerializer<T>>
  ): RegistrySupplier<RecipeSerializer<T>> {
    return RECIPE_SERIALIZERS.register(
      id,
      supplier as Supplier<RecipeSerializer<*>>
    ) as RegistrySupplier<RecipeSerializer<T>>
  }

  fun <T: ParticleOptions> registerParticleType(
    id: String,
    supplier: Supplier<ParticleType<T>>
  ): RegistrySupplier<ParticleType<T>> {
    return PARTICLE_TYPES.register(id, supplier as Supplier<ParticleType<*>>) as RegistrySupplier<ParticleType<T>>
  }

  fun <T: AbstractContainerMenu> registerMenu(id: String, supplier: Supplier<MenuType<T>>): RegistrySupplier<MenuType<T>> {
    return MENUS.register(id, supplier as Supplier<MenuType<*>>) as RegistrySupplier<MenuType<T>>
  }

  fun registerSound(id: String, supplier: Supplier<SoundEvent>): RegistrySupplier<SoundEvent> {
    return SOUNDS.register(id, supplier)
  }

  fun location(path: String) = ResourceLocation.fromNamespaceAndPath(id, path)

  fun <T> registerTag(registry: ResourceKey<Registry<T>>, path: ResourceLocation) = TagKey.create(registry, path)
  fun <T> registerTag(registry: ResourceKey<Registry<T>>, path: String) = registerTag(registry, location(path))
  fun registerItemTag(path: ResourceLocation) = registerTag(Registries.ITEM, path)
  fun registerItemTag(path: String) = registerItemTag(location(path))
  fun registerBlockTag(path: ResourceLocation) = registerTag(Registries.BLOCK, path)
  fun registerBlockTag(path: String) = registerBlockTag(location(path))
}