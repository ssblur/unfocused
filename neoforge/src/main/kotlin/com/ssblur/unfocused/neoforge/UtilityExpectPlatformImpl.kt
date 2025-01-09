@file:JvmName("UtilityExpectPlatformImpl")
package com.ssblur.unfocused.neoforge

import net.minecraft.world.item.CreativeModeTab
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment

@Suppress("unused")
object UtilityExpectPlatformImpl {
    @JvmStatic
    fun creativeTabBuilder(): CreativeModeTab.Builder = CreativeModeTab.builder()

    @JvmStatic
    fun isClient(): Boolean = FMLEnvironment.dist == Dist.CLIENT
    @JvmStatic
    fun isServer(): Boolean = FMLEnvironment.dist == Dist.DEDICATED_SERVER
}