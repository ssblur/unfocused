package com.ssblur.unfocused.registry

import com.mojang.datafixers.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.HolderOwner
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import java.util.*
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.stream.Stream

class RegistrySupplier<T>(
    val supplier: Supplier<T>,
    val location: ResourceLocation? = null,
    val registry: ResourceKey<out Registry<T>>? = null,
): Supplier<T>, Holder<T> {
    var value: T? = null
    var key: ResourceKey<T>? = null

    init {
        if(location != null && registry != null)
            key = ResourceKey.create(registry, location)
    }

    override fun get(): T {
        if(value == null)
            value = supplier.get()
        return value!!
    }

    override fun value(): T = get()
    override fun isBound() = true
    override fun tags(): Stream<TagKey<T>> {
        TODO("Not yet implemented")
    }
    override fun unwrap(): Either<ResourceKey<T>, T> = Either.right(value)
    override fun unwrapKey(): Optional<ResourceKey<T>> = Optional.ofNullable(key)
    override fun kind() = Holder.Kind.DIRECT
    override fun canSerializeIn(holderOwner: HolderOwner<T>) = true
    override fun `is`(tagKey: TagKey<T>): Boolean {
        TODO("Not yet implemented")
    }
    override fun `is`(predicate: Predicate<ResourceKey<T>>) = key?.let { predicate.test(it) } ?: false
    override fun `is`(resourceKey: ResourceKey<T>) = resourceKey == key
    override fun `is`(resourceLocation: ResourceLocation) = resourceLocation == location
    override fun `is`(holder: Holder<T>) = location?.let { holder.`is`(it) } ?:  (isBound && holder.isBound && value() == holder.value())
}