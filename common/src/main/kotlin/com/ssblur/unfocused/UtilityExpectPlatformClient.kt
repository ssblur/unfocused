@file:JvmName("UtilityExpectPlatformClient")
package com.ssblur.unfocused

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.client.color.block.BlockColor
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

@Suppress("unused", "unused_parameter")
object UtilityExpectPlatformClient {
  @ExpectPlatform
  @JvmStatic
  fun registerColor(color: DyedItemColor, vararg items: Supplier<ItemLike>) {
    throw AssertionError()
  }

  @ExpectPlatform
  @JvmStatic
  fun registerColor(color: BlockColor, vararg blocks: Supplier<Block>) {
    throw AssertionError()
  }
}