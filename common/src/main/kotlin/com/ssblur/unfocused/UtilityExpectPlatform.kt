@file:JvmName("UtilityExpectPlatform")
package com.ssblur.unfocused

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.world.item.CreativeModeTab

object UtilityExpectPlatform {
    @ExpectPlatform
    @JvmStatic
    fun creativeTabBuilder(): CreativeModeTab.Builder = throw AssertionError()
}