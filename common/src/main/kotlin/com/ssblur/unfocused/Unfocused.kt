package com.ssblur.unfocused

import com.ssblur.unfocused.constructors.Effect
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

object Unfocused: ModInitializer("unfocused") {
    var isNeoForge = false
    var isFabric = false

    data class TestMessage(val i: Int)
    val testMessage = NetworkManager.registerC2S(location("test_message"), TestMessage::class) { payload, receiver ->
        println(payload)
    }
    fun init() {
        registerBlockWithItem("test_block") { Block(BlockBehaviour.Properties.of()) }
        registerEffect("test_effect") { Effect { println(it) } }
    }
}