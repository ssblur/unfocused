package com.ssblur.unfocused.registry

import com.ssblur.unfocused.registry.Registry.Subscriber
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey

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

  val ITEM = RegistryType(Registries.ITEM)
  val BLOCK = RegistryType(Registries.BLOCK)
  val EFFECTS = RegistryType(Registries.MOB_EFFECT)
  val BLOCK_ENTITIES = RegistryType(Registries.BLOCK_ENTITY_TYPE)
  val DATA_COMPONENTS = RegistryType(Registries.DATA_COMPONENT_TYPE)
  val ENTITIES = RegistryType(Registries.ENTITY_TYPE)
  val FEATURES = RegistryType(Registries.FEATURE)
  val RECIPE_TYPES = RegistryType(Registries.RECIPE_TYPE)
  val RECIPES = RegistryType(Registries.RECIPE)
  val RECIPE_SERIALIZERS = RegistryType(Registries.RECIPE_SERIALIZER)
  val LOOT_FUNCTION_TYPES = RegistryType(Registries.LOOT_FUNCTION_TYPE)
  val LOOT_CONDITION_TYPES = RegistryType(Registries.LOOT_CONDITION_TYPE)
  val TRIGGER_TYPES = RegistryType(Registries.TRIGGER_TYPE)
  val CREATIVE_TABS = RegistryType(Registries.CREATIVE_MODE_TAB)
  val PARTICLE_TYPES = RegistryType(Registries.PARTICLE_TYPE)
  val MENUS = RegistryType(Registries.MENU)
  val ARMOR = RegistryType(Registries.ARMOR_MATERIAL)
}