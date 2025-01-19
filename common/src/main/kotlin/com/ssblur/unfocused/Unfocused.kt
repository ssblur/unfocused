package com.ssblur.unfocused

import org.apache.logging.log4j.LogManager

object Unfocused: ModInitializer("unfocused") {
    var isNeoForge = false
    var isFabric = false
    val LOGGER = LogManager.getLogger(id)!!


    fun init() {

    }
}