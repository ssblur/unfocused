package com.ssblur.unfocused.data

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import kotlin.reflect.KClass

open class DataLoaderListener<T: Any>(path: String, val type: KClass<T>, val callback: DataLoader<T>): SimpleJsonResourceReloadListener(GSON, path) {
    companion object {
        var GSON: Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    }

    override fun apply(
        map: MutableMap<ResourceLocation, JsonElement>,
        resourceManager: ResourceManager,
        profilerFiller: ProfilerFiller
    ) {
        map.forEach { (location, obj) ->
            if(obj.isJsonObject) {
                val value = obj.asJsonObject
                if(value.has("disabled") && value.get("disabled").asBoolean) return@forEach
                callback.load(GSON.fromJson(obj, type.javaObjectType), location)
            }
        }
    }


}