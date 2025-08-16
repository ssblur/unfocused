@file:JvmName("UtilityExpectPlatformClientImpl")

package com.ssblur.unfocused.neoforge

import net.minecraft.client.color.block.BlockColor
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

@Suppress("unused")
object UtilityExpectPlatformClientImpl {
  val ITEM_COLORS: MutableList<Pair<DyedItemColor, Supplier<ItemLike>>> = mutableListOf()
  val BLOCK_COLORS: MutableList<Pair<BlockColor, Supplier<Block>>> = mutableListOf()

  @JvmStatic
  fun registerColor(color: DyedItemColor, vararg items: Supplier<ItemLike>) {
    items.forEach {
      ITEM_COLORS += Pair(color, it)
    }
  }

  @JvmStatic
  fun registerColor(color: BlockColor, vararg blocks: Supplier<Block>) {
    blocks.forEach {
      BLOCK_COLORS += Pair(color, it)
    }
  }
}