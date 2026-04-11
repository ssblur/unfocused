package com.ssblur.unfocused.fabric.events

import com.ssblur.unfocused.data.DataLoaderListener
import com.ssblur.unfocused.data.DataLoaderRegistry
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object UnfocusedModData {
  fun init() {
    DataLoaderRegistry.register {
      ResourceManagerHelper.get(PackType.SERVER_DATA)
        .registerReloadListener(object: IdentifiableResourceReloadListener {
          override fun getFabricId() = it.mod.location(it.path)
          override fun reload(
            sharedState: PreparableReloadListener.SharedState,
            executor: Executor,
            preparationBarrier: PreparableReloadListener.PreparationBarrier,
            executor2: Executor
          ): CompletableFuture<Void> {
            return DataLoaderListener(it.path, it.type, it.failEasy) { value, location: Identifier ->
              it.loader.load(value, location)
            }.reload(sharedState, executor, preparationBarrier, executor2)
          }
        })
    }
  }
}