package com.ssblur.unfocused.screen.renderable

import com.ssblur.unfocused.Unfocused
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines

class SinglePageBackground(x: Int, y: Int, w: Int, h: Int,) :
  PositionedRenderable(x, y, w, h, true) {
  override fun draw(
    guiGraphics: GuiGraphicsExtractor,
    mouseX: Int,
    mouseY: Int,
    f: Float
  ) {
    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, w, h)
  }

  companion object {
    val TEXTURE = Unfocused.location("container/single_page")
  }
}