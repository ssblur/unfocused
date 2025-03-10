package com.ssblur.unfocused.serialization

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import kotlin.reflect.KClass

@Suppress("unused")
object KClassCodec {
  fun <T: Any> streamCodec(type: KClass<T>): StreamCodec<RegistryFriendlyByteBuf, T> {
    val gson = UnfocusedJson.streamBuilder(type).create()
    return StreamCodec.of(
      { buffer, payload ->
        buffer.writeUtf(gson.toJson(payload, type.javaObjectType).drop(1).dropLast(1))
      },
      { buffer ->
        val input = buffer.readUtf()
        gson.fromJson("{$input}", type.javaObjectType)
      }
    )
  }

  fun <T: Any> codec(type: KClass<T>): Codec<T> {
    val gson = UnfocusedJson.builder().create()
    return RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<T> ->
      instance.group(
        Codec.STRING.fieldOf("v").forGetter{ gson.toJson(it, type.javaObjectType) }
      ).apply(instance) { values -> gson.fromJson(values, type.javaObjectType) }
    }
  }
}