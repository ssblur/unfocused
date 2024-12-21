package com.ssblur.unfocused.extension

import net.minecraft.server.MinecraftServer

object MinecraftServerExtension {
    private var internalRunnableInitialized = mutableMapOf<MinecraftServer, Boolean>()
    private var MinecraftServer.runnableInitialized: Boolean
        get() = internalRunnableInitialized[this] ?: false
        set(value) { internalRunnableInitialized[this] = value }

    private var runnableQueue = mutableListOf<Runnable>()

    fun MinecraftServer.runOnce(runnable: Runnable) {
        if(!runnableInitialized) {
            runnableQueue = mutableListOf()
            this.addTickable {
                runnableQueue.forEach { it.run() }
                runnableQueue = mutableListOf()
            }
        }
        runnableInitialized = true

        runnableQueue += runnable
    }
}