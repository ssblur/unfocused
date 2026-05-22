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
import net.minecraft.resources.Identifier
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
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
      type.members.forEach {
        if(type.primaryConstructor?.parameters?.any{ t -> it.name == t.name } == true) {
          val codec = codec(it.returnType)
          val value = it.call(input)
          codec.encode(value, ops, output).ifSuccess { result ->
            output = ops.set(output, it.name, result)
          }.ifError { error ->
            output = ops.set(output, it.name, null)
          }
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
      val args = type.primaryConstructor?.parameters?.map {
        val codec = codec(it.type)
        val o = ops.get(input, it.name).result().getOrNull()?.let { d -> codec.decode(ops, d) }
        o?.ifError { println("Error decoding: $it") }
        o?.result()?.getOrNull()?.first
      }
      return DataResult.success(Pair(
        type.primaryConstructor?.call(*(args?.toTypedArray() ?: arrayOf())),
        input
      ))
    } catch (e: Exception) {
      e.printStackTrace()
      return DataResult.error{ e.message }
    }
  }

  companion object {
    private val CODECS: MutableMap<KType, Codec<*>> = mutableMapOf()

    fun <T: Any> streamCodec(type: KType): StreamCodec<RegistryFriendlyByteBuf, T> {
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

    fun <T: Any> streamCodec(type: KClass<T>): StreamCodec<RegistryFriendlyByteBuf, T> {
      return streamCodec(type.createType(
        type.typeParameters.map(::projection)
      ))
    }

    @Suppress("UNCHECKED_CAST")
    fun codec(type: KType): Codec<Any> {
      if(type.jvmErasure == List::class) {
        return codec(type.arguments.first().type!!).listOf() as Codec<Any>
      }
      if(type.jvmErasure == Map::class)
        return Codec.unboundedMap(
          codec(type.arguments[0].type!!),
          codec(type.arguments[1].type!!)
        ) as Codec<Any>
      if(type.jvmErasure == Identifier::class) return Identifier.CODEC as Codec<Any>
      if(type.jvmErasure == String::class) return Codec.STRING as Codec<Any>
      if(type.jvmErasure == Int::class) return Codec.INT as Codec<Any>
      if(type.jvmErasure == Long::class) return Codec.LONG as Codec<Any>
      if(type.jvmErasure == Byte::class) return Codec.BYTE as Codec<Any>
      if(type.jvmErasure == Boolean::class) return Codec.BOOL as Codec<Any>
      if(CODECS[type] != null) return CODECS[type]!! as Codec<Any>
      CODECS[type] = KClassCodec(type.jvmErasure)
      return CODECS[type]!! as Codec<Any>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> codec(type: KClass<T>): Codec<T> {
      return codec(type.createType(
        type.typeParameters.map(::projection)
      )) as Codec<T>
    }

    private fun projection(type: KTypeParameter): KTypeProjection {
      return KTypeProjection(type.variance, type.starProjectedType)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> primitiveCodec(value: T): PrimitiveCodec<T>? {
      if(value is Int) return Codec.INT as PrimitiveCodec<T>
      if(value is String) return Codec.STRING as PrimitiveCodec<T>
      if(value is Long) return Codec.LONG as PrimitiveCodec<T>
      if(value is Boolean) return Codec.BOOL as PrimitiveCodec<T>
      return null
    }
  }
}