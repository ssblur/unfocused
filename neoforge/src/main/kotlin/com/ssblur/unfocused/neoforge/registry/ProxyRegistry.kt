package com.ssblur.unfocused.neoforge.registry

import com.ssblur.unfocused.registry.RegistryTypes
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

class ProxyRegistry<T>(val registry: ResourceKey<Registry<T>>, val registryType: RegistryTypes.RegistryType<T>) {
    private val registries: HashMap<String, DeferredRegister<T>> = hashMapOf()
    private var bus: IEventBus? = null
    fun get(id: String): DeferredRegister<T> {
        if(registries.containsKey(id)) return registries[id]!!
        registries[id] = DeferredRegister.create(registry, id)
        if(bus != null) registries[id]?.register(bus!!)
        return registries[id]!!
    }

    fun register(bus: IEventBus) {
        registries.forEach{ it.value.register(bus) }
        this.bus = bus
        registryType.subscribe { location, supplier ->
            get(location.namespace).register(location.path, supplier)
        }
    }
}