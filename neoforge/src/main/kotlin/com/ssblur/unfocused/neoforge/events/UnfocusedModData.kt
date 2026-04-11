package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.data.DataLoaderListener
import com.ssblur.unfocused.data.DataLoaderRegistry
import net.minecraft.resources.Identifier
import net.neoforged.neoforge.event.AddReloadListenerEvent


object UnfocusedModData {
  fun event(event: AddReloadListenerEvent) {
    DataLoaderRegistry.register {
      event.addListener(DataLoaderListener(it.path, it.type, it.failEasy) { value, location: Identifier ->
        it.loader.load(value, location)
      })
    }
  }
}