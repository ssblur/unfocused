@file:JvmName("UtilityExpectPlatform")

package com.ssblur.unfocused

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.block.Block
import java.nio.file.Path
import java.util.function.Supplier

@Suppress("unused", "unused_parameter")
object UtilityExpectPlatform {
  @ExpectPlatform
  @JvmStatic
  fun creativeTabBuilder(): CreativeModeTab.Builder = throw AssertionError()

  @ExpectPlatform
  @JvmStatic
  fun isClient(): Boolean = throw AssertionError()

  @ExpectPlatform
  @JvmStatic
  fun isServer(): Boolean = throw AssertionError()

//  @Environment(EnvType.CLIENT)
//  @ExpectPlatform
//  @JvmStatic
//  fun registerColor(color: DyedItemColor, vararg items: Supplier<ItemLike>) {
//    throw AssertionError()
//  }

//  @Environment(EnvType.CLIENT)
  @ExpectPlatform
  @JvmStatic
  fun registerColor(color: BlockTintSource, vararg blocks: Supplier<Block>) {
    throw AssertionError()
  }


  @ExpectPlatform
  @JvmStatic
  fun configDir(): Path {
    throw AssertionError()
  }

  @ExpectPlatform
  @JvmStatic
  fun isModLoaded(name: String): Boolean {
    throw AssertionError()
  }
}