@file:JvmName("UtilityExpectPlatform")

package com.ssblur.unfocused

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.world.item.CreativeModeTab
import java.nio.file.Path

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