package com.ssblur.unfocused.extension

import com.ssblur.unfocused.extension.MinecraftServerExtension.runOnce
import net.minecraft.server.level.ServerLevel

@Suppress("unused")
object ServerLevelExtension {
  fun ServerLevel.runOnce(runnable: Runnable) {
    server.runOnce(runnable)
  }
}