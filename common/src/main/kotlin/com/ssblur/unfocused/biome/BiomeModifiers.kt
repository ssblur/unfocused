package com.ssblur.unfocused.biome

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.data.DataLoaderRegistry.registerDataLoader
import com.ssblur.unfocused.event.LoadEvent
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.levelgen.GenerationStep

object BiomeModifiers {
  class BiomeFeature(
    override val biome: String,
    val step: GenerationStep.Decoration,
    val feature: ResourceLocation,
  ): BiomeModifier()

  class BiomeCarver(
    override val biome: String,
    val step: GenerationStep.Decoration,
    val carver: ResourceLocation
  ): BiomeModifier()

  class BiomeSpawner(
    override val biome: String,
    val spawners: List<Spawner>,
  ): BiomeModifier()

  abstract class BiomeModifier {
    abstract val biome: String
    fun isValid(holder: Holder<Biome>): Boolean =
        if(biome.startsWith("#"))
          holder.`is`(TagKey.create(Registries.BIOME, ResourceLocation.parse(biome.substring(1))))
        else
          holder.`is`(ResourceLocation.parse(biome))
  }

  data class Spawner(val type: ResourceLocation, val weight: Int, val minCount: Int, val maxCount: Int, val category: MobCategory)

  val featureEvent = LoadEvent<Pair<ResourceLocation, BiomeFeature>>()
  val carverEvent = LoadEvent<Pair<ResourceLocation, BiomeCarver>>()
  val spawnEvent = LoadEvent<Pair<ResourceLocation, BiomeSpawner>>()

  init {
    Unfocused.registerDataLoader("unfocused/features", BiomeFeature::class) { modification, location ->
      featureEvent.callback(Pair(location, modification))
    }
    Unfocused.registerDataLoader("unfocused/carvers", BiomeCarver::class) { modification, location ->
      carverEvent.callback(Pair(location, modification))
    }
    Unfocused.registerDataLoader("unfocused/spawns", BiomeSpawner::class) { modification, location ->
      spawnEvent.callback(Pair(location, modification))
    }
  }
}