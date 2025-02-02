package com.ssblur.unfocused.rendering

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

@Suppress("unused")
object BlockEntityRendering:
  SimpleEvent<BlockEntityRendering.BlockEntityAndRenderer<out BlockEntity>>(retroactive = true) {
  data class BlockEntityAndRenderer<T: BlockEntity>(
    val type: RegistrySupplier<BlockEntityType<T>>,
    val renderer: BlockEntityRendererProvider<T>
  )

  fun <T: BlockEntity> ModInitializer.registerBlockEntityRenderer(
    type: RegistrySupplier<BlockEntityType<T>>,
    renderer: BlockEntityRendererProvider<T>
  ) {
    BlockEntityRendering.callback(BlockEntityAndRenderer(type, renderer))
  }
}