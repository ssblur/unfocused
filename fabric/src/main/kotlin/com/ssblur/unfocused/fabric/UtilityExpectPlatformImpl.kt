@file:JvmName("UtilityExpectPlatformImpl")

package com.ssblur.unfocused.fabric

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.minecraft.client.color.block.BlockTintSource
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.block.Block
import java.nio.file.Path
import java.util.function.Supplier

@Suppress("unused")
object UtilityExpectPlatformImpl {
  @JvmStatic
  fun creativeTabBuilder(): CreativeModeTab.Builder = FabricCreativeModeTab.builder()

  @JvmStatic
  fun isClient(): Boolean = FabricLoaderImpl.INSTANCE.environmentType == EnvType.CLIENT

  @JvmStatic
  fun isServer(): Boolean = FabricLoaderImpl.INSTANCE.environmentType == EnvType.SERVER

  @JvmStatic
  fun registerColor(color: BlockTintSource, vararg blocks: Supplier<Block>) {
    BlockColorRegistry.register(listOf(color), *blocks.map { it.get() }.toTypedArray())
  }

  @JvmStatic
  fun configDir(): Path = FabricLoader.getInstance().configDir

  @JvmStatic
  fun isModLoaded(name: String): Boolean {
    return FabricLoader.getInstance().isModLoaded(name)
  }
}