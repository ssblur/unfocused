package com.ssblur.unfocused.network

import com.google.gson.Gson
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.KClass

@Suppress("unused")
class KClassPacket<T: Any>(val location: ResourceLocation, val type: KClass<T>, val value: Any): CustomPacketPayload {
    init {
        types[location] = type
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return CustomPacketPayload.Type(location)
    }

    companion object {
        val types: HashMap<ResourceLocation, KClass<*>> = hashMapOf()
        private val gson: Gson = Gson()

        // Uses GSON under the hood, want to switch to CBOR once this lib is more stable.
        // but this gets something working.
        fun <T: Any> codec(location: ResourceLocation, type: KClass<T>): StreamCodec<RegistryFriendlyByteBuf, KClassPacket<*>> {
            return StreamCodec.of(
                { buffer, payload ->
                    buffer.writeUtf(gson.toJson(payload.value, type.javaObjectType))
                },
                { buffer ->
                    val value: T = gson.fromJson(buffer.readUtf(), type.javaObjectType)
                    println(value)
                    KClassPacket(location, type, value)
                }
            )
        }

        fun type(location: ResourceLocation): CustomPacketPayload.Type<out CustomPacketPayload> {
            return CustomPacketPayload.Type(location)
        }
    }
}