package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.network.KClassPacket
import com.ssblur.unfocused.network.NetworkManager
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import kotlin.reflect.KClass

object UnfocusedModNetworkingClient {
    fun init() {
        NetworkManager.subscribeToS2CRegistration(
            { type ->
                val id: CustomPacketPayload.Type<KClassPacket<*>> = CustomPacketPayload.Type(type.location)
    //            PayloadTypeRegistry.playS2C().register(id, KClassPacket.codec(type.location, type.type))

                ClientPlayNetworking.registerGlobalReceiver(id) { payload, context ->
                    NetworkManager.s2cTypes.forEach {
                        if(it.location.equals(type.location))
                            (it.receiver as NetworkManager.S2CReceiver<Any>).receive(payload)
                    }
                }
            },
            { location, type, payload, players ->
                players.forEach { ServerPlayNetworking.send(it as ServerPlayer, KClassPacket(location, type, payload)) }
            }
        )
    }
}