package com.ssblur.unfocused.registry

import com.mojang.datafixers.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.HolderOwner
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrNull

@Suppress("unused", "UNCHECKED_CAST")
class RegistrySupplier<T>(
    val supplier: Supplier<T>,
    val location: ResourceLocation? = null,
    val registryKey: ResourceKey<out Registry<T>>? = null,
): Supplier<T>, Holder<T> {
    var value: T? = null
    var key: ResourceKey<T>? = null
    var tags: MutableSet<TagKey<T>> = mutableSetOf()
    private var pending: MutableList<Consumer<T>> = mutableListOf()

    init {
        if(location != null && registryKey != null)
            key = ResourceKey.create(registryKey, location)
    }

    override fun get(): T {
        if(value == null) {
            value = supplier.get()
            pending.forEach{ it.accept(value!!) }
            pending = mutableListOf()
        }
        return value!!
    }

    fun then(consumer: Consumer<T>) {
        if(value == null)
            pending += consumer
        else
            consumer.accept(value!!)
    }

    fun ref() = location?.let { BuiltInRegistries.REGISTRY.get(registryKey!!.location())?.getHolder(it)?.getOrNull() } as Holder.Reference<T>
    override fun value() = get()!!
    override fun isBound() = true
    override fun tags() = tags.stream()
    override fun unwrap(): Either<ResourceKey<T>, T> = Either.right(value)
    override fun unwrapKey() = Optional.ofNullable(key)
    override fun kind() = Holder.Kind.DIRECT
    override fun canSerializeIn(holderOwner: HolderOwner<T>) = true
    override fun `is`(tagKey: TagKey<T>) = tags().anyMatch { it == tagKey }
    override fun `is`(predicate: Predicate<ResourceKey<T>>) = key?.let { predicate.test(it) } ?: false
    override fun `is`(resourceKey: ResourceKey<T>) = resourceKey == key
    override fun `is`(resourceLocation: ResourceLocation) = resourceLocation == location
    @Deprecated("Deprecated in Java",
        ReplaceWith("equals")
    )
    override fun `is`(holder: Holder<T>) = location?.let { holder.`is`(it) } ?:  (isBound && holder.isBound && value() == holder.value())
}