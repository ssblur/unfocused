package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.network.KClassPacket
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent


@Suppress("UNCHECKED_CAST")
object UnfocusedModNetworking {
  @SubscribeEvent
  fun register(event: RegisterPayloadHandlersEvent) {
    val registrar = event.registrar("1")
    NetworkManager.subscribeToC2SRegistration(
      { type ->
        val id: CustomPacketPayload.Type<KClassPacket<*>> = CustomPacketPayload.Type(type.location)
        registrar.playToServer(id, KClassPacket.codec(type.location, type.type)) { payload, context ->
          NetworkManager.c2sTypes.forEach {
            if (it.location == type.location)
              (it.receiver as NetworkManager.C2SReceiver<Any>).receive(payload.value, context.player() as ServerPlayer)
          }
        }
      },
      { location, type, payload -> PacketDistributor.sendToServer(KClassPacket(location, type, payload)) }
    )

    NetworkManager.subscribeToS2CRegistration(
      { type ->
        val id: CustomPacketPayload.Type<KClassPacket<*>> = CustomPacketPayload.Type(type.location)
        registrar.playToClient(id, KClassPacket.codec(type.location, type.type)) { payload, context ->
          NetworkManager.s2cTypes.forEach {
            if (it.location == type.location)
              (it.receiver as NetworkManager.S2CReceiver<Any>).receive(payload.value)
          }
        }
      },
      { location, type, payload, players ->
        players.forEach {
          PacketDistributor.sendToPlayer(
            it as ServerPlayer,
            KClassPacket(location, type, payload)
          )
        }
      }
    )

    NetworkManager.s2cQueue.register { (players, packet) ->
      players.forEach {
        PacketDistributor.sendToPlayer(it as ServerPlayer, packet)
      }
    }

    NetworkManager.c2sQueue.register {
      PacketDistributor.sendToServer(it)
    }
  }
}