package com.ssblur.unfocused.data

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.serialization.UnfocusedJson
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.ExtraCodecs
import net.minecraft.util.profiling.ProfilerFiller
import kotlin.reflect.KClass

open class DataLoaderListener<T: Any>(path: String, val type: KClass<T>, val failEasy: Boolean = false, val callback: DataLoader<T>):
  SimpleJsonResourceReloadListener<JsonElement>(ExtraCodecs.JSON, FileToIdConverter.json(path)) {
  companion object {
    var GSON: Gson = UnfocusedJson.builder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create()
  }

  override fun apply(
    map: MutableMap<ResourceLocation, JsonElement>,
    resourceManager: ResourceManager,
    profilerFiller: ProfilerFiller
  ) {
    map.forEach { (location, obj) ->
      if (obj.isJsonObject) {
        val value = obj.asJsonObject
        if (value.has("disabled") && value.get("disabled").asBoolean) return@forEach
        if (value.has("required") && !Unfocused.isModLoaded(value.get("required").asString)) return@forEach

        try {
          callback.load(GSON.fromJson(obj, type.javaObjectType), location)
        } catch (e: JsonSyntaxException) {
          if (!failEasy) throw e
        }
      }
    }
  }


}