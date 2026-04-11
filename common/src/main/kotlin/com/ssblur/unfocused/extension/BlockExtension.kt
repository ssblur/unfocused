package com.ssblur.unfocused.extension

import com.ssblur.unfocused.event.Event
import com.ssblur.unfocused.event.SimpleEvent
import com.ssblur.unfocused.extension.BlockExtension.renderType
import net.minecraft.client.renderer.chunk.ChunkSectionLayer
import net.minecraft.world.level.block.Block

@Suppress("unused")
object BlockExtension {
  val event = SimpleEvent<Pair<Block, ChunkSectionLayer>>(true)
  fun register(subscriber: Event.Listener<Pair<Block, ChunkSectionLayer>>) {
    event.register(subscriber)
  }

  /**
   * Sets a block's RenderType in an environment-safe way.
   * This only works on Fabric at the moment.
   * For NeoForge, the "render_type" field in model JSONs is preferred.
   * @param renderType The RenderType specifying the rendering layer used for this block.
   * @return this, for chaining.
   */
  fun Block.renderType(renderType: ChunkSectionLayer): Block {
    event.callback(Pair(this, renderType))
    return this
  }
}