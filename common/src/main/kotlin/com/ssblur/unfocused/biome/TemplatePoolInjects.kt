package com.ssblur.unfocused.biome

import com.mojang.datafixers.util.Pair
import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool

object TemplatePoolInjects {
  data class PoolInject(
    val pool: Identifier,
    val elements: List<WeightedPoolElement>
  )

  data class WeightedPoolElement(
    val element: PoolElement,
    val weight: Int?
  )

  data class PoolElement(
    val location: String,
    val processors: Identifier?,
    val projection: StructureTemplatePool.Projection
  )

  val injects = Unfocused.registerSimpleDataLoader("unfocused/template_pool_injects", PoolInject::class)

  fun inject(server: MinecraftServer) {
    val poolRegistry = server.registryAccess().get(Registries.TEMPLATE_POOL).orElseThrow().value()
    val processorLists = server.registryAccess().get(Registries.PROCESSOR_LIST).orElseThrow().value()

    injects.forEach { (_, poolInject) ->
      val pool = poolRegistry.get(poolInject.pool).get().value()
      val templates = pool.templates

      val empty = templates.takeLastWhile {
        it.first.type == StructurePoolElementType.EMPTY
      }
      templates.dropLastWhile {
        it.first.type == StructurePoolElementType.EMPTY
      }
//      val rawTemplates = pool.rawTemplates

      poolInject.elements.forEach { element ->
        val poolElement = SinglePoolElement.legacy(
            element.element.location,
            processorLists.get(ResourceKey.create(
              Registries.PROCESSOR_LIST,
              element.element.processors ?: Identifier.parse("minecraft:empty")
            )).get()
          ).apply(element.element.projection)
        (0..(element.weight ?: 2)).forEach { _ -> templates.add(Pair(poolElement, element.weight)) }
        pool.templates.addAll(empty)
//        pool.rawTemplates = rawTemplates + Pair(poolElement, element.weight)
      }
    }
  }

  fun init() {}
}