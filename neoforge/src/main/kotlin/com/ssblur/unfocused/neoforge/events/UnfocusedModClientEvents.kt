package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.event.client.MouseScrollEvent
import net.minecraft.client.Minecraft
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.common.NeoForge
import org.joml.Vector2d

object UnfocusedModClientEvents {
    fun clientTickEventBefore(event: ClientTickEvent.Pre) = Minecraft.getInstance().level?.let(ClientLevelTickEvent.Before::callback)
    fun clientTickEventAfter(event: ClientTickEvent.Post) = Minecraft.getInstance().level?.let(ClientLevelTickEvent.After::callback)
    fun clientScrollEvent(event: InputEvent.MouseScrollingEvent) {
        MouseScrollEvent.callback(MouseScrollEvent.KeyPress(Minecraft.getInstance(), Vector2d(event.scrollDeltaX, event.scrollDeltaY), MouseScrollEvent))
        if(MouseScrollEvent.isCancelled()) event.isCanceled = true
    }

    fun register() {
        NeoForge.EVENT_BUS.addListener(::clientTickEventAfter)
        NeoForge.EVENT_BUS.addListener(::clientTickEventBefore)
        NeoForge.EVENT_BUS.addListener(::clientScrollEvent)
    }
}