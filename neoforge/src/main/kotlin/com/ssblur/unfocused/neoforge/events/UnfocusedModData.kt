package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.data.DataLoaderListener
import com.ssblur.unfocused.data.DataLoaderRegistry
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.event.AddReloadListenerEvent


object UnfocusedModData {
  fun event(event: AddReloadListenerEvent) {
    DataLoaderRegistry.register {
      event.addListener(DataLoaderListener(it.path, it.type) { value, location: ResourceLocation ->
        it.loader.load(value, location)
      })
    }
  }
}