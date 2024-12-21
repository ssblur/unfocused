package com.ssblur.unfocused.network

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.KClass

@Suppress("unused")
class KClassPacket<T: Any>(val location: ResourceLocation, type: KClass<T>, val value: Any): CustomPacketPayload {
    init {
        types[location] = type
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
        return CustomPacketPayload.Type(location)
    }

    companion object {
        val types: HashMap<ResourceLocation, KClass<*>> = hashMapOf()

        @OptIn(ExperimentalSerializationApi::class)
        fun <T: Any> codec(location: ResourceLocation, type: KClass<T>): StreamCodec<RegistryFriendlyByteBuf, KClassPacket<*>> {
            return StreamCodec.of(
                { buffer, payload ->
                    buffer.writeBytes(Cbor.encodeToByteArray(payload.value))
                },
                { buffer ->
                    val value = Cbor.decodeFromByteArray(buffer.readByteArray()) as KClassPacket<*>
                    KClassPacket(location, type, value)
                }
            )
        }

        fun type(location: ResourceLocation): CustomPacketPayload.Type<out CustomPacketPayload> {
            return CustomPacketPayload.Type(location)
        }
    }
}