@file:JvmName("UtilityExpectPlatformImpl")
package com.ssblur.unfocused.neoforge

import net.minecraft.world.item.CreativeModeTab

@Suppress("unused")
object UtilityExpectPlatformImpl {
    @JvmStatic
    fun creativeTabBuilder(): CreativeModeTab.Builder = CreativeModeTab.builder()
}