@file:JvmName("UtilityExpectPlatformImpl")

package com.ssblur.unfocused.neoforge

import net.minecraft.client.color.block.BlockColor
import net.minecraft.client.color.item.ItemColor
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Path
import java.util.function.Supplier

@Suppress("unused")
object UtilityExpectPlatformImpl {
  @JvmStatic
  fun creativeTabBuilder(): CreativeModeTab.Builder = CreativeModeTab.builder()

  @JvmStatic
  fun isClient(): Boolean = FMLEnvironment.dist == Dist.CLIENT

  @JvmStatic
  fun isServer(): Boolean = FMLEnvironment.dist == Dist.DEDICATED_SERVER

  val ITEM_COLORS: MutableList<Pair<ItemColor, Supplier<ItemLike>>> = mutableListOf()
  val BLOCK_COLORS: MutableList<Pair<BlockColor, Supplier<Block>>> = mutableListOf()

  @JvmStatic
  fun registerColor(color: ItemColor, vararg items: Supplier<ItemLike>) {
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

  @JvmStatic
  fun configDir(): Path = FMLPaths.CONFIGDIR.get()

  @JvmStatic
  fun isModLoaded(name: String): Boolean {
    return ModList.get().isLoaded(name)
  }
}