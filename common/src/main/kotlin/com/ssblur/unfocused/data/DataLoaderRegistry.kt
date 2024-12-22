package com.ssblur.unfocused.data

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.Event
import kotlin.reflect.KClass

@Suppress("unused")
object DataLoaderRegistry {
    data class DataLoaderEntry<T: Any>(val path: String, val type: KClass<T>, val loader: DataLoader<T>, val mod: ModInitializer)
    private val event = Event<DataLoaderEntry<in Any>>(true)
    fun register(subscriber: Event.Listener<DataLoaderEntry<in Any>>) {
        event.register(subscriber)
    }

    fun ModInitializer.registerDataLoader(path: String, type: KClass<in Any>, dataLoader: DataLoader<Any>) {
        val entry = DataLoaderEntry(path, type, dataLoader, this)
        event.callback(entry)
    }
}