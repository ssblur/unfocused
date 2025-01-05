@file:JvmName("UtilityExpectPlatformImpl")
package com.ssblur.unfocused.fabric

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.minecraft.world.item.CreativeModeTab

@Suppress("unused")
object UtilityExpectPlatformImpl {
    @JvmStatic
    fun creativeTabBuilder(): CreativeModeTab.Builder = FabricItemGroup.builder()
}