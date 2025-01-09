@file:JvmName("UtilityExpectPlatform")
package com.ssblur.unfocused

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.world.item.CreativeModeTab

@Suppress("unused")
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
}