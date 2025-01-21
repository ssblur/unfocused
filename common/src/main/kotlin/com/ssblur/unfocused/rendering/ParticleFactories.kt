package com.ssblur.unfocused.rendering

import com.mojang.datafixers.util.Either
import com.ssblur.unfocused.event.SimpleEvent
import com.ssblur.unfocused.registry.RegistrySupplier
import com.ssblur.unfocused.rendering.ParticleFactories.ParticleTypeAndProvider
import com.ssblur.unfocused.rendering.ParticleFactories.ParticleTypeAndProviderProvider
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType

typealias ParticleChoice = Either<ParticleTypeAndProvider, ParticleTypeAndProviderProvider>

@Suppress("unchecked_cast", "unused")
object ParticleFactories: SimpleEvent<ParticleChoice>(retroactive = true) {
    data class ParticleTypeAndProvider(val particle: ParticleType<in ParticleOptions>, val provider: ParticleProvider<in ParticleOptions>)
    fun <T: ParticleOptions> RegistrySupplier<ParticleType<T>>.registerFactory(provider: ParticleProvider<T>) {
        this.then{
            ParticleFactories.callback(
                Either.left(ParticleTypeAndProvider(it as ParticleType<in ParticleOptions>, provider as ParticleProvider<in ParticleOptions>))
            )
        }
    }

    data class ParticleTypeAndProviderProvider(val particle: ParticleType<in ParticleOptions>, val provider: (SpriteSet) -> ParticleProvider.Sprite<in ParticleOptions>)
    fun <T: ParticleOptions> RegistrySupplier<ParticleType<T>>.registerFactory(provider: (SpriteSet) -> ParticleProvider.Sprite<T>) {
        this.then{
            ParticleFactories.callback(
                Either.right(ParticleTypeAndProviderProvider(
                    it as ParticleType<in ParticleOptions>,
                    provider as (SpriteSet) -> ParticleProvider.Sprite<in ParticleOptions>)
                )
            )
        }
    }
}