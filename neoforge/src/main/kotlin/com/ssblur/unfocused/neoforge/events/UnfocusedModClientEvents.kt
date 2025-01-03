package com.ssblur.unfocused.neoforge.events

import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.event.client.MouseScrollEvent
import com.ssblur.unfocused.rendering.BlockEntityRendering
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.event.InputEvent
import net.neoforged.neoforge.common.NeoForge
import org.joml.Vector2d

@Suppress("UNCHECKED_CAST", "unused")
object UnfocusedModClientEvents {
    fun clientTickEventBefore(event: ClientTickEvent.Pre) = Minecraft.getInstance().level?.let(ClientLevelTickEvent.Before::callback)
    fun clientTickEventAfter(event: ClientTickEvent.Post) = Minecraft.getInstance().level?.let(ClientLevelTickEvent.After::callback)
    fun clientScrollEvent(event: InputEvent.MouseScrollingEvent) {
        MouseScrollEvent.callback(MouseScrollEvent.KeyPress(Minecraft.getInstance(), Vector2d(event.scrollDeltaX, event.scrollDeltaY), MouseScrollEvent))
        if(MouseScrollEvent.isCancelled()) event.isCanceled = true
    }
    fun registerEntityRendererEvent(event: EntityRenderersEvent.RegisterRenderers) {

        BlockEntityRendering.register{ pair ->
            event.registerBlockEntityRenderer<BlockEntity>(pair.type, pair.renderer as BlockEntityRendererProvider<BlockEntity>)
        }
    }

    fun register() {
        NeoForge.EVENT_BUS.addListener(::clientTickEventAfter)
        NeoForge.EVENT_BUS.addListener(::clientTickEventBefore)
        NeoForge.EVENT_BUS.addListener(::clientScrollEvent)
        NeoForge.EVENT_BUS.addListener(::registerEntityRendererEvent)
    }
}