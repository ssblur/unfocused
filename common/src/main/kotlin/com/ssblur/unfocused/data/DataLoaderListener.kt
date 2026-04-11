package com.ssblur.unfocused.data

import com.ssblur.unfocused.serialization.KClassCodec
import net.minecraft.resources.FileToIdConverter
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import kotlin.reflect.KClass

open class DataLoaderListener<T: Any>(path: String, val type: KClass<T>, val failEasy: Boolean = false, val callback: DataLoader<T>):
  SimpleJsonResourceReloadListener<T>(
    KClassCodec.codec(type),
    FileToIdConverter.json(path)
  ) {
  override fun apply(
    values: Map<Identifier, T>,
    resourceManager: ResourceManager,
    profilerFiller: ProfilerFiller
  ) {
    values.forEach { (id, value) ->
      callback.load(value, id)
    }
  }
}