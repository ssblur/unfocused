@file:JvmName("UtilityExpectPlatformImpl")

package com.ssblur.unfocused.neoforge

import net.minecraft.world.item.CreativeModeTab
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Path

@Suppress("unused")
object UtilityExpectPlatformImpl {
  @JvmStatic
  fun creativeTabBuilder(): CreativeModeTab.Builder = CreativeModeTab.builder()

  @JvmStatic
  fun isClient(): Boolean = FMLEnvironment.dist == Dist.CLIENT

  @JvmStatic
  fun isServer(): Boolean = FMLEnvironment.dist == Dist.DEDICATED_SERVER

  @JvmStatic
  fun configDir(): Path = FMLPaths.CONFIGDIR.get()

  @JvmStatic
  fun isModLoaded(name: String): Boolean {
    return ModList.get().isLoaded(name)
  }
}