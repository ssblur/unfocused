package com.ssblur.unfocused.neoforge

import com.ssblur.unfocused.UnfocusedClient
import com.ssblur.unfocused.neoforge.events.UnfocusedModClientEvents
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(value = "unfocused", dist = [Dist.CLIENT])
object UnfocusedModNeoForgeClient {
  init {
    val bus = MOD_BUS
    UnfocusedModClientEvents.register(bus)
    UnfocusedClient.init()
  }
}