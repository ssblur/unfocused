package com.ssblur.unfocused.fabric.events

import com.ssblur.unfocused.data.DataLoaderListener
import com.ssblur.unfocused.data.DataLoaderRegistry
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object UnfocusedModData {
    fun init() {
        DataLoaderRegistry.register{
            ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(object: IdentifiableResourceReloadListener{
                override fun reload(
                    barrier: PreparableReloadListener.PreparationBarrier,
                    manager: ResourceManager,
                    filler: ProfilerFiller,
                    filler2: ProfilerFiller,
                    executor: Executor,
                    executor2: Executor
                ): CompletableFuture<Void> =
                    DataLoaderListener(it.path, it.type) { value, location: ResourceLocation ->
                        it.loader.load(value, location)
                    }.reload(barrier, manager, filler, filler2, executor, executor2)
                override fun getFabricId() = it.mod.location(it.path)
            })
        }
    }
}