package com.ssblur.unfocused.biome

import com.mojang.datafixers.util.Pair
import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import com.ssblur.unfocused.mixin.StructureTemplatePoolAccessor
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
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

  fun inject(
    holder: Holder<StructureTemplatePool>,
    rawTemplates: List<Pair<StructurePoolElement, Int>>,
    templates: ObjectArrayList<StructurePoolElement>,
    pool: StructureTemplatePool
  ) {
    injects.filter { (_, value) -> holder.`is`(value.pool) }.forEach { (_, value) ->
      value.elements.forEach { element ->
        val poolElement = SinglePoolElement.legacy(element.element.location)
          .apply(element.element.projection)
        (0..(element.weight ?: 2)).forEach { _ ->
          templates.add(poolElement)
        }
        (pool as StructureTemplatePoolAccessor).rawTemplates =
          (rawTemplates + Pair(poolElement, element.weight!!))
      }
    }
  }

  fun register() {}
}