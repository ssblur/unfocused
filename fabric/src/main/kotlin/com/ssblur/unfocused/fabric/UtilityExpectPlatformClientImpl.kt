@file:JvmName("UtilityExpectPlatformClientImpl")

package com.ssblur.unfocused.fabric

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.minecraft.client.color.block.BlockColor
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

@Suppress("unused")
object UtilityExpectPlatformClientImpl {
  @JvmStatic
  fun registerColor(color: BlockColor, vararg blocks: Supplier<Block>) {
    ColorProviderRegistry.BLOCK.register(color, *blocks.map { it.get() }.toTypedArray())
  }
}