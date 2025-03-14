package com.ssblur.unfocused.network

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import org.jetbrains.annotations.ApiStatus.Internal
import kotlin.reflect.KClass

@Suppress("unused")
object NetworkManager {
  fun interface S2CReceiver<T: Any> {
    fun receive(payload: T)
  }

  fun interface S2CSubscriber {
    fun post(type: S2CType)
  }

  fun interface S2CMessenger {
    fun send(location: ResourceLocation, type: KClass<*>, payload: Any, players: List<Player>)
  }

  data class S2CType(
    val location: ResourceLocation,
    val type: KClass<out Any>,
    val receiver: S2CReceiver<out Any>
  )

  val s2cTypes: ArrayList<S2CType> = arrayListOf()
  val s2cSubscribers: ArrayList<S2CSubscriber> = arrayListOf()
  val s2cMessengers: ArrayList<S2CMessenger> = arrayListOf()

  data class PlayerPacket(val players: List<Player>, val packet: CustomPacketPayload)

  val s2cQueue = SimpleEvent<PlayerPacket>()
  val c2sQueue = SimpleEvent<CustomPacketPayload>()

  /**
   * Register a new Client-to-Server message type.
   * @param location A unique ResourceLocation for this message
   * @param type Any KClass representing this message.
   * @param receiver A callback which is run when the client receives a message.
   * @return A callback which can be used to send this Network message
   */
  fun <T: Any> registerS2C(
    location: ResourceLocation,
    type: KClass<T>,
    receiver: S2CReceiver<T>
  ): (T, List<Player>) -> Unit {
    val message = S2CType(location, type, receiver)
    s2cTypes += message
    s2cSubscribers.forEach { it.post(message) }
    return { payload, players ->
      s2cMessengers.forEach { it.send(location, type, payload, players) }
    }
  }

  @Internal
  fun subscribeToS2CRegistration(subscriber: S2CSubscriber, messenger: S2CMessenger? = null) {
    s2cTypes.forEach { subscriber.post(it) }
    s2cSubscribers += subscriber
    messenger?.let { s2cMessengers += it }
  }


  fun interface C2SReceiver<T: Any> {
    fun receive(payload: T, player: ServerPlayer)
  }

  fun interface C2SSubscriber {
    fun post(type: C2SType)
  }

  fun interface C2SMessenger {
    fun send(location: ResourceLocation, type: KClass<*>, payload: Any)
  }

  data class C2SType(
    val location: ResourceLocation,
    val type: KClass<out Any>,
    val receiver: C2SReceiver<out Any>,
  )

  val c2sTypes: ArrayList<C2SType> = arrayListOf()
  val c2sSubscribers: ArrayList<C2SSubscriber> = arrayListOf()
  val c2sMessengers: ArrayList<C2SMessenger> = arrayListOf()

  /**
   * Register a new Client-to-Server message type.
   * @param location A unique ResourceLocation for this message
   * @param type Any KClass representing this message.
   * @param receiver A callback which is run when the server receives a message.
   * @return A callback which can be used to send this Network message
   */
  fun <T: Any> registerC2S(location: ResourceLocation, type: KClass<T>, receiver: C2SReceiver<T>): (T) -> Unit {
    val message = C2SType(location, type, receiver)
    c2sTypes += message
    c2sSubscribers.forEach { it.post(message) }
    return { payload: T ->
      c2sMessengers.forEach { it.send(location, type, payload) }
    }
  }

  @Internal
  fun subscribeToC2SRegistration(subscriber: C2SSubscriber, messenger: C2SMessenger? = null) {
    c2sTypes.forEach { subscriber.post(it) }
    c2sSubscribers += subscriber
    messenger?.let { c2sMessengers += it }
  }

  fun CustomPacketPayload.sendToServer() {
    c2sQueue.callback(this)
  }

  fun <T: PacketListener> Packet<T>.sendToServer() {
    (this as CustomPacketPayload).sendToServer()
  }

  fun CustomPacketPayload.sendToClient(players: Iterable<Player>) {
    s2cQueue.callback(PlayerPacket(players.toList(), this))
  }

  fun <T: PacketListener> Packet<T>.sendToClient(players: Iterable<Player>) {
    (this as CustomPacketPayload).sendToClient(players)
  }

  fun CustomPacketPayload.sendToClient(vararg players: Player) {
    this.sendToClient(players.toList())
  }

  fun <T: PacketListener> Packet<T>.sendToClient(vararg players: Player) {
    (this as CustomPacketPayload).sendToClient(players.toList())
  }
}