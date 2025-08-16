package com.ssblur.unfocused.biome

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
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
    server.registryAccess().listRegistries().forEach { println(it) }
//    val poolRegistry = server.registryAccess().get(Registries.TEMPLATE_POOL).get()
//    val processorLists = server.registryAccess().get(Registries.PROCESSOR_LIST).get()
//
//    injects.forEach { (resourceLocation, poolInject) ->
//      val pool = poolRegistry.value().get(poolInject.pool)!! as StructureTemplatePoolAccessor
//      val templates = pool.templates
//      val rawTemplates = pool.rawTemplates
//
//      poolInject.elements.forEach { element ->
//        val poolElement = SinglePoolElement.legacy(
//            element.element.location,
//            processorLists.value().get(ResourceKey.create(
//              Registries.PROCESSOR_LIST,
//              element.element.processors ?: ResourceLocation.parse("minecraft:empty")
//            )).orElseThrow()
//          ).apply(element.element.projection)
//        for(i in 0..(element.weight ?: 2))
//          templates.add(poolElement)
//        pool.rawTemplates = rawTemplates + Pair(poolElement, element.weight)
//      }
//    }
  }
}