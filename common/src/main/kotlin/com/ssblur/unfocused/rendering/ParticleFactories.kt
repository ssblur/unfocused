package com.ssblur.unfocused.rendering

import com.ssblur.unfocused.event.SimpleEvent
import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType

@Suppress("unchecked_cast", "unused")
object ParticleFactories: SimpleEvent<ParticleFactories.ParticleTypeAndProvider>(retroactive = true) {
    data class ParticleTypeAndProvider(val particle: ParticleType<in ParticleOptions>, val provider: ParticleProvider<in ParticleOptions>)
    fun <T: ParticleOptions> RegistrySupplier<ParticleType<T>>.registerFactory(provider: ParticleProvider<T>) {
        this.then{
            ParticleFactories.callback(
                ParticleTypeAndProvider(it as ParticleType<in ParticleOptions>, provider as ParticleProvider<in ParticleOptions>)
            )
        }
    }
}