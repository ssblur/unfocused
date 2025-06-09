package com.ssblur.unfocused.neoforge.biome

import com.google.gson.JsonElement
import com.mojang.serialization.*
import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.biome.BiomeModifiers
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.MobSpawnSettings
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo
import java.util.stream.Stream

class RegistryAwareBiomeModifier(val registryOps: RegistryOps<JsonElement>): BiomeModifier {
  override fun modify(arg: Holder<Biome>, phase: BiomeModifier.Phase, builder: ModifiableBiomeInfo.BiomeInfo.Builder) {
    try {
      val featureGetter = registryOps.getter(Registries.PLACED_FEATURE).get()
      val carverGetter = registryOps.getter(Registries.CONFIGURED_CARVER).get()
      val entityGetter = registryOps.getter(Registries.ENTITY_TYPE).get()

      if(phase == BiomeModifier.Phase.ADD) {
        BiomeModifiers.featureEvent.register { (key, value) ->
          val feature = featureGetter.getOrThrow(ResourceKey.create(Registries.PLACED_FEATURE, value.feature))
          if(value.isValid(arg))
            builder.generationSettings.addFeature(value.step, feature)
        }

        BiomeModifiers.carverEvent.register { (key, value) ->
          val carver = carverGetter.getOrThrow(ResourceKey.create(Registries.CONFIGURED_CARVER, value.carver))
          if(value.isValid(arg))
            builder.generationSettings.addCarver(value.step, carver)
        }

        BiomeModifiers.spawnEvent.register { (key, value) ->
          for(spawn in value.spawners) {
            val entity = entityGetter.getOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, spawn.type))
            if(value.isValid(arg))
              builder.mobSpawnSettings.addSpawn(spawn.category, MobSpawnSettings.SpawnerData(
                entity.value(),
                spawn.weight,
                spawn.minCount,
                spawn.maxCount
              ))
          }
        }
      }
    } catch (e: Exception) {
      Unfocused.LOGGER.warn("RegistryAwareBiomeModifier serialized or deserialized in a non-json context!")
      Unfocused.LOGGER.warn("This should not happen!")
      Unfocused.LOGGER.warn(e)
    }
  }

  override fun codec(): MapCodec<out BiomeModifier> = CODEC

  companion object {
    val CODEC = object: MapCodec<RegistryAwareBiomeModifier>() {
      override fun <T : Any?> keys(p0: DynamicOps<T>?): Stream<T> = Stream.empty()

      override fun <T : Any?> decode(p0: DynamicOps<T>?, p1: MapLike<T>?): DataResult<RegistryAwareBiomeModifier> {
        return if(p0 is RegistryOps) DataResult.success(RegistryAwareBiomeModifier(p0 as RegistryOps<JsonElement>))
          else DataResult.error { "RegistryAwareBiomeModifiers can only be decoded via RegistryOps" }
      }

      override fun <T : Any?> encode(
        p0: RegistryAwareBiomeModifier?,
        p1: DynamicOps<T>?,
        p2: RecordBuilder<T>?
      ): RecordBuilder<T> = Encoder.empty<RegistryAwareBiomeModifier>().encode(p0, p1, p2)
    }
  }
}