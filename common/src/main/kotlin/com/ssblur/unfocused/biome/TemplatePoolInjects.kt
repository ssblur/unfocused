package com.ssblur.unfocused.biome

import com.mojang.datafixers.util.Pair
import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import com.ssblur.unfocused.mixin.StructureTemplatePoolAccessor
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool

object TemplatePoolInjects {
  data class PoolInject(
    val pool: ResourceLocation,
    val elements: List<WeightedPoolElement>
  )

  data class WeightedPoolElement(
    val element: PoolElement,
    val weight: Int?
  )

  data class PoolElement(
    val location: String,
    val processors: ResourceLocation?,
    val projection: StructureTemplatePool.Projection
  )

  val injects = Unfocused.registerSimpleDataLoader("unfocused/template_pool_injects", PoolInject::class)

  fun inject(server: MinecraftServer) {
    val poolRegistry = server.registryAccess().registry(Registries.TEMPLATE_POOL).orElseThrow()
    val processorLists = server.registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow()

    injects.forEach { (resourceLocation, poolInject) ->
      val pool = poolRegistry.get(poolInject.pool)!!
      val templates = (pool as StructureTemplatePoolAccessor).templates
      val rawTemplates = (pool as StructureTemplatePoolAccessor).rawTemplates

      poolInject.elements.forEach { element ->
        val poolElement = (
          if(element.element.processors == null)
            SinglePoolElement.single(element.element.location)
          else
            SinglePoolElement.single(
              element.element.location,
              processorLists.getHolderOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, element.element.processors))
            )
          ).apply(element.element.projection)
        for(i in 0..(element.weight ?: 2))
          templates.add(poolElement)
        (pool as StructureTemplatePoolAccessor).rawTemplates = rawTemplates + Pair(poolElement, element.weight)
      }
    }
  }
}