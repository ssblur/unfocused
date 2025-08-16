package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.data.DataLoaderListener
import com.ssblur.unfocused.data.DataLoaderRegistry
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.event.AddServerReloadListenersEvent


object UnfocusedModData {
  fun event(event: AddServerReloadListenersEvent) {
    DataLoaderRegistry.register {
      event.addListener(
        it.mod.location(it.path),
        DataLoaderListener(it.path, it.type, it.failEasy) { value, location: ResourceLocation ->
          it.loader.load(value, location)
        }
      )
    }
  }
}