@file:JvmName("UtilityExpectPlatformClientImpl")

package com.ssblur.unfocused.neoforge

import net.minecraft.client.color.block.BlockColor
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

@Suppress("unused")
object UtilityExpectPlatformClientImpl {
  val BLOCK_COLORS: MutableList<Pair<BlockColor, Supplier<Block>>> = mutableListOf()

  @JvmStatic
  fun registerColor(color: BlockColor, vararg blocks: Supplier<Block>) {
    blocks.forEach {
      BLOCK_COLORS += Pair(color, it)
    }
  }
}