package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.data.DataLoaderListener
import com.ssblur.unfocused.data.DataLoaderRegistry
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.event.AddServerReloadListenersEvent


object UnfocusedModData {
  fun event(event: AddServerReloadListenersEvent) {
    DataLoaderRegistry.register {
      event.addListener(Unfocused.location("unfocused_" + it.path.replace('/', '_')),
        DataLoaderListener(it.path, it.type, it.failEasy) { value, location: Identifier ->
        it.loader.load(value, location)
      })
    }
  }
}