package com.ssblur.unfocused.registry

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier


class Registry<A>(val id: String, val key: ResourceKey<Registry<A>>) {
  fun interface Subscriber<T> {
    fun onRegistered(location: ResourceLocation, supplier: Supplier<T>)
  }

  class RegistryEntry<T>(val id: String, val supplier: Supplier<T>) {
    var registered = false
  }

  val entries: ArrayList<RegistryEntry<A>> = arrayListOf()
  val subscribers: ArrayList<Subscriber<A>> = arrayListOf()

  fun register(id: String, supplier: Supplier<A>): RegistrySupplier<A> {
    val registrySupplier = RegistrySupplier(supplier, ResourceLocation.fromNamespaceAndPath(this.id, id), key)
    entries += RegistryEntry(id, registrySupplier)
    subscribers.forEach { it.onRegistered(ResourceLocation.fromNamespaceAndPath(this.id, id), registrySupplier) }
    return registrySupplier
  }

  fun subscribe(subscriber: Subscriber<A>) {
    entries.forEach {
      if (!it.registered)
        subscriber.onRegistered(ResourceLocation.fromNamespaceAndPath(id, it.id), it.supplier)
      it.registered = true
    }
    subscribers += subscriber
  }
}