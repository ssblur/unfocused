package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.network.KClassPacket
import com.ssblur.unfocused.network.NetworkManager
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

@Suppress("UNCHECKED_CAST")
object UnfocusedModNetworkingClient {
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
      { location, type, payload -> ClientPlayNetworking.send(KClassPacket(location, type, payload)) }
    )

    NetworkManager.c2sQueue.register {
      ClientPlayNetworking.send(it)
    }
  }
}