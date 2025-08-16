package com.ssblur.unfocused.biome

import com.mojang.datafixers.util.Pair
import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import com.ssblur.unfocused.mixin.StructureTemplatePoolAccessor
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.RegistryLayer
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
    val worldgen = server.registries().getLayer(RegistryLayer.STATIC)

    val poolRegistry = worldgen.get(Registries.TEMPLATE_POOL)
    if(poolRegistry.isEmpty)
      Unfocused.LOGGER.error("Template Pool registry does not appear to be initialized, aborting template injects.")


    val processorLists = worldgen.get(Registries.PROCESSOR_LIST)
    if(processorLists.isEmpty)
      Unfocused.LOGGER.error("Processor List registry does not appear to be initialized, aborting template injects.")

    if(poolRegistry.isEmpty || processorLists.isEmpty) return

    injects.forEach { (_, poolInject) ->
      @Suppress("CAST_NEVER_SUCCEEDS")
      val pool = poolRegistry.get().value().get(poolInject.pool)!! as StructureTemplatePoolAccessor
      val templates = pool.templates
      val rawTemplates = pool.rawTemplates

      poolInject.elements.forEach { element ->
        val poolElement = SinglePoolElement.legacy(
            element.element.location,
            processorLists.get().value().get(ResourceKey.create(
              Registries.PROCESSOR_LIST,
              element.element.processors ?: ResourceLocation.parse("minecraft:empty")
            )).orElseThrow()
          ).apply(element.element.projection)
        (0..(element.weight ?: 2)).forEach { _ ->
          templates.add(poolElement)
        }
        pool.rawTemplates = rawTemplates + Pair(poolElement, element.weight!!)
      }
    }
  }
}