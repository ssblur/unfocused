@file:JvmName("UtilityExpectPlatformImpl")
package com.ssblur.unfocused.fabric

import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.minecraft.world.item.CreativeModeTab

@Suppress("unused")
object UtilityExpectPlatformImpl {
    @JvmStatic
    fun creativeTabBuilder(): CreativeModeTab.Builder = FabricItemGroup.builder()

    @JvmStatic
    fun isClient(): Boolean = FabricLoaderImpl.INSTANCE.environmentType == EnvType.CLIENT
    @JvmStatic
    fun isServer(): Boolean = FabricLoaderImpl.INSTANCE.environmentType == EnvType.SERVER
}