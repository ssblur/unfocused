package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.neoforge.biome.RegistryAwareBiomeModifier
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

@Suppress("unused")
object UnfocusedAdditionalRegistration {
  val BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Unfocused.id)
  val FEATURES = BIOME_MODIFIER_SERIALIZERS.register("features") { ->
    RegistryAwareBiomeModifier.CODEC
  }

  fun register(bus: IEventBus) {
    BIOME_MODIFIER_SERIALIZERS.register(bus)
  }
}