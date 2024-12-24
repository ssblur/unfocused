package com.ssblur.unfocused.event.client

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.client.Minecraft
import org.joml.Vector2d

@Suppress("unused")
object MouseScrollEvent: SimpleEvent<MouseScrollEvent.KeyPress>(false, true) {
    data class KeyPress(val minecraft: Minecraft, val amount: Vector2d, val event: MouseScrollEvent) {
        fun cancel() = event.cancel()
    }
}