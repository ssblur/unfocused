package com.ssblur.unfocused.extension

import com.ssblur.unfocused.event.Event
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.block.Block

@Suppress("unused")
object BlockExtension {
    val event = Event<Pair<Block, RenderType>>(true)
    fun register(subscriber: Event.Listener<Pair<Block, RenderType>>) {
        event.register(subscriber)
    }

    /**
     * Sets a block's RenderType in an environment-safe way.
     * This only works on Fabric at the moment.
     * For NeoForge, the "render_type" field in model JSONs is preferred.
     * @param renderType The RenderType specifying the rendering layer used for this block.
     * @return this, for chaining.
     */
    fun Block.renderType(renderType: RenderType): Block {
        event.callback(Pair(this, renderType))
        return this
    }
}