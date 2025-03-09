package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.network.KClassPacket
import com.ssblur.unfocused.network.NetworkManager
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

@Suppress("unchecked_cast")
object UnfocusedModNetworking {
  fun init() {
    NetworkManager.subscribeToC2SRegistration(
      { type ->
        val id: CustomPacketPayload.Type<KClassPacket<*>> = CustomPacketPayload.Type(type.location)
        PayloadTypeRegistry.playC2S().register(id, KClassPacket.codec(type.location, type.type))
        ServerPlayNetworking.registerGlobalReceiver(id) { payload, context ->
          NetworkManager.c2sTypes.forEach {
            if (it.location == type.location)
              (it.receiver as NetworkManager.C2SReceiver<Any>).receive(payload.value, context.player())
          }
        }
      },
    )

    NetworkManager.subscribeToS2CRegistration(
      { type ->
        val id: CustomPacketPayload.Type<KClassPacket<*>> = CustomPacketPayload.Type(type.location)
        PayloadTypeRegistry.playS2C().register(id, KClassPacket.codec(type.location, type.type))
      },
      { location, type, payload, players ->
        players.forEach { ServerPlayNetworking.send(it as ServerPlayer, KClassPacket(location, type, payload)) }
      }
    )

    NetworkManager.s2cQueue.register { (players, packet) ->
      players.forEach {
        ServerPlayNetworking.send(it as ServerPlayer, packet)
      }
    }
  }
}