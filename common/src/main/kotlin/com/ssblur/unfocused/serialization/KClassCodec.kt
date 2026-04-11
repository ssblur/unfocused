package com.ssblur.unfocused.serialization

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.codecs.PrimitiveCodec
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.createType
import kotlin.reflect.jvm.jvmErasure

@Suppress("unused")
class KClassCodec<T: Any>(val type: KClass<T>): Codec<T> {
  override fun <I> encode(
    input: T,
    ops: DynamicOps<I>,
    prefix: I
  ): DataResult<I> {
    try {
      var output = prefix
      type.members.sortedBy { it.name }.forEach {
        val value = it.call(input)
        if(value != null) {
          val codec = primitiveCodec(value)
          if(codec != null) {
            val o = codec.encode(value, ops, output)
            if(o.result().isPresent) output = o.result().get()
          } else {
            val valueType = value::class

            @Suppress("UNCHECKED_CAST")
            val o = (codec(valueType) as KClassCodec<Any>).encode(value, ops, output)
            if (o.result().isPresent) output = o.result().get()
          }
        } else {
          ops.set(output, it.name, null)
        }
      }
      return DataResult.success(output)
    } catch (e: Exception) {
      return DataResult.error{ e.message }
    }
  }

  override fun <I: Any> decode(
    ops: DynamicOps<I>,
    input: I
  ): DataResult<Pair<T, I>> {
    try {
      val data = type.createInstance()
      type.members.sortedBy { it.name }.forEach {
        val codec = primitiveTypeCodec(it.returnType)
        if(codec != null) {
          val o = ops.get(input, it.name).result().getOrNull()?.let { d -> codec.decode(ops, d) }
          if(o?.result()?.isPresent == true) it.call(data, o.result().get())
        } else {
          val valueType = it.returnType.jvmErasure

          @Suppress("UNCHECKED_CAST")
          val o = ops.get(input, it.name).result().getOrNull()?.let { d ->
            (codec(valueType) as KClassCodec<Any>).decode(ops, d)
          }
          if (o?.result()?.isPresent == true) it.call(data, o.result().get())
        }
      }
      return DataResult.success(Pair(data, input))
    } catch (e: Exception) {
      return DataResult.error{ e.message }
    }
  }

  companion object {
    private val CODECS: MutableMap<KClass<Any>, KClassCodec<Any>> = mutableMapOf()

    fun <T: Any> streamCodec(type: KClass<T>): StreamCodec<RegistryFriendlyByteBuf, T> {
      val codec = codec(type)
      return StreamCodec.of(
        { buffer, payload ->
          val encoded = codec.encode(payload, NbtOps.INSTANCE, CompoundTag())
          if(encoded.result().isPresent) buffer.writeNbt(encoded.result().get())
        },
        { buffer ->
          val decoded = codec.decode(NbtOps.INSTANCE, buffer.readNbt()!!)
          @Suppress("UNCHECKED_CAST") if(decoded.result().isPresent)
            decoded.result().get() as T
          else
            throw Exception(decoded.error().get().message())
        }
      )
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> codec(type: KClass<T>): KClassCodec<T> {
      if(CODECS[type as KClass<Any>] != null)  return CODECS[type] as KClassCodec<T>
      CODECS[type] = KClassCodec(type)
      return CODECS[type] as KClassCodec<T>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> primitiveCodec(value: T): PrimitiveCodec<T>? {
      if(value is Int) return Codec.INT as PrimitiveCodec<T>
      if(value is String) return Codec.STRING as PrimitiveCodec<T>
      if(value is Long) return Codec.LONG as PrimitiveCodec<T>
      if(value is Boolean) return Codec.BOOL as PrimitiveCodec<T>
      return null
    }

    @Suppress("UNCHECKED_CAST")
    fun primitiveTypeCodec(value: KType): PrimitiveCodec<Any>? {
      if(value.classifier == Int::class.createType()) return Codec.INT as PrimitiveCodec<Any>
      if(value.classifier == String::class.createType()) return Codec.STRING as PrimitiveCodec<Any>
      if(value.classifier == Long::class.createType()) return Codec.LONG as PrimitiveCodec<Any>
      if(value.classifier == Boolean::class.createType()) return Codec.BOOL as PrimitiveCodec<Any>
      return null
    }
  }
}