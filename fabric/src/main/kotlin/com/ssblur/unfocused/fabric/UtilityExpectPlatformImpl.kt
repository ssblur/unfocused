@file:JvmName("UtilityExpectPlatformImpl")

package com.ssblur.unfocused.fabric

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.minecraft.world.item.CreativeModeTab
import java.nio.file.Path

@Suppress("unused")
object UtilityExpectPlatformImpl {
  @JvmStatic
  fun creativeTabBuilder(): CreativeModeTab.Builder = FabricItemGroup.builder()

  @JvmStatic
  fun isClient(): Boolean = FabricLoaderImpl.INSTANCE.environmentType == EnvType.CLIENT

  @JvmStatic
  fun isServer(): Boolean = FabricLoaderImpl.INSTANCE.environmentType == EnvType.SERVER

  @JvmStatic
  fun configDir(): Path = FabricLoader.getInstance().configDir

  @JvmStatic
  fun isModLoaded(name: String): Boolean {
    return FabricLoader.getInstance().isModLoaded(name)
  }
}