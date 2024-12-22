package com.ssblur.unfocused.network

import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

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

        fun <T: Any> codec(location: ResourceLocation, type: KClass<T>): StreamCodec<RegistryFriendlyByteBuf, KClassPacket<*>> {
            val gson = GsonBuilder() // While I'm using Gson I might as well trim it down a bit
                .setFieldNamingStrategy {
                    if(type.declaredMemberProperties.indexOfFirst { f -> f.name == it.name } >= 0)
                        type.declaredMemberProperties.indexOfFirst { f -> f.name == it.name }.toString()
                        else it.name
                }
                .disableHtmlEscaping()
                .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .create()
            return StreamCodec.of(
                { buffer, payload ->
                    buffer.writeUtf(gson.toJson(payload.value, type.javaObjectType).drop(1).dropLast(1))
                },
                { buffer ->
                    val input = buffer.readUtf()
                    val value: T = gson.fromJson("{$input}", type.javaObjectType)
                    KClassPacket(location, type, value)
                }
            )
        }

        fun type(location: ResourceLocation): CustomPacketPayload.Type<out CustomPacketPayload> {
            return CustomPacketPayload.Type(location)
        }
    }
}