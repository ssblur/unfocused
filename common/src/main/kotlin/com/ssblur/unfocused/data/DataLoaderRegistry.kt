package com.ssblur.unfocused.data

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.Event
import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.KClass

@Suppress("unused")
object DataLoaderRegistry {
    data class DataLoaderEntry<T: Any>(val path: String, val type: KClass<T>, val loader: DataLoader<T>, val mod: ModInitializer)
    private val event = SimpleEvent<DataLoaderEntry<in Any>>(true)
    fun register(subscriber: Event.Listener<DataLoaderEntry<in Any>>) {
        event.register(subscriber)
    }

    fun <T: Any> ModInitializer.registerDataLoader(path: String, type: KClass<T>, dataLoader: DataLoader<T>) {
        val entry = DataLoaderEntry(path, type, dataLoader, this)
        event.callback(entry as DataLoaderEntry<in Any>)
    }

    fun <T: Any> ModInitializer.registerSimpleDataLoader(path: String, type: KClass<T>): MutableMap<ResourceLocation, T> {
        val map: MutableMap<ResourceLocation, T> = mutableMapOf()
        registerDataLoader(path, type as KClass<Any>) { resource, location ->
            map[location] = resource as T
        }
        return map
    }
}