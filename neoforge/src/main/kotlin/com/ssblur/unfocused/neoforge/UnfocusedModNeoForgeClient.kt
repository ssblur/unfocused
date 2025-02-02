package com.ssblur.unfocused.neoforge

import com.ssblur.unfocused.neoforge.events.UnfocusedModClientEvents
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod

@Mod(value = "unfocused", dist = [Dist.CLIENT])
class UnfocusedModNeoForgeClient(bus: IEventBus) {
  init {
    UnfocusedModClientEvents.register(bus)
  }
}