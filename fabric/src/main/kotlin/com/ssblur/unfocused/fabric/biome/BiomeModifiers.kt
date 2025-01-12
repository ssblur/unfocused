package com.ssblur.unfocused.fabric.biome

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.data.DataLoaderRegistry.registerDataLoader
import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.resources.ResourceLocation

object BiomeModifiers {
    data class BiomeModification(
        val type: String,
        val biomes: String,
        val step: String?,
        val features: String?,
        val spawners: List<Spawner>?,
        val carvers: String?
    )
    data class Spawner(val type: String, val weight: Int, val minCount: Int, val maxCount: Int)
    val event = SimpleEvent<Pair<ResourceLocation, BiomeModification>>(retroactive = true)
    init {
        Unfocused.registerDataLoader("neoforge/biome_modifier", BiomeModification::class) { modification, location ->
            event.callback(Pair(location, modification))
        }
    }
}