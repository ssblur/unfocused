package com.ssblur.unfocused.event.client

import com.ssblur.unfocused.event.Event
import net.minecraft.client.Minecraft
import org.joml.Vector2d

@Suppress("unused")
object MouseScrollEvent: Event<MouseScrollEvent.KeyPress>(false, true) {
    data class KeyPress(val minecraft: Minecraft, val amount: Vector2d, val event: MouseScrollEvent) {
        fun cancel() = event.cancel()
    }
}