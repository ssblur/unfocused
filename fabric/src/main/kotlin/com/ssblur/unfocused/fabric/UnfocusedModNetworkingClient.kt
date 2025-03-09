package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.network.KClassPacket
import com.ssblur.unfocused.network.NetworkManager
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

@Suppress("UNCHECKED_CAST")
object UnfocusedModNetworkingClient {
  fun init() {
    NetworkManager.subscribeToC2SRegistration(
      { },
      { location, type, payload -> ClientPlayNetworking.send(KClassPacket(location, type, payload)) }
    )

    NetworkManager.subscribeToS2CRegistration(
      { type ->
        val id: CustomPacketPayload.Type<KClassPacket<*>> = CustomPacketPayload.Type(type.location)
        ClientPlayNetworking.registerGlobalReceiver(id) { payload, context ->
          NetworkManager.s2cTypes.forEach {
            if (it.location == type.location)
              (it.receiver as NetworkManager.S2CReceiver<Any>).receive(payload.value)
          }
        }
      },
    )

    NetworkManager.c2sQueue.register {
      ClientPlayNetworking.send(it)
    }
  }
}