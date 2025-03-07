@file:JvmName("UtilityExpectPlatformImpl")

package com.ssblur.unfocused.fabric

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.minecraft.client.color.block.BlockColor
import net.minecraft.client.color.item.ItemColor
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.nio.file.Path
import java.util.function.Supplier

@Suppress("unused")
object UtilityExpectPlatformImpl {
  @JvmStatic
  fun creativeTabBuilder(): CreativeModeTab.Builder = FabricItemGroup.builder()

  @JvmStatic
  fun isClient(): Boolean = FabricLoaderImpl.INSTANCE.environmentType == EnvType.CLIENT

  @JvmStatic
  fun isServer(): Boolean = FabricLoaderImpl.INSTANCE.environmentType == EnvType.SERVER

  @Environment(EnvType.CLIENT)
  @JvmStatic
  fun registerColor(color: ItemColor, vararg items: Supplier<ItemLike>) {
    ColorProviderRegistry.ITEM.register(color, *items.map { it.get() }.toTypedArray())
  }

  @Environment(EnvType.CLIENT)
  @JvmStatic
  fun registerColor(color: BlockColor, vararg blocks: Supplier<Block>) {
    ColorProviderRegistry.BLOCK.register(color, *blocks.map { it.get() }.toTypedArray())
  }

  @JvmStatic
  fun configDir(): Path = FabricLoader.getInstance().configDir

  @JvmStatic
  fun isModLoaded(name: String): Boolean {
    return FabricLoader.getInstance().isModLoaded(name)
  }
}