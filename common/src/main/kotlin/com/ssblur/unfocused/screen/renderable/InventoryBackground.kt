package com.ssblur.unfocused.screen.renderable

import com.ssblur.unfocused.Unfocused
import net.minecraft.client.gui.GuiGraphics

@Suppress("unused")
class InventoryBackground(x: Int, y: Int, w: Int, h: Int,) :
  PositionedRenderable(x, y, w, h, true) {
  override fun draw(
    guiGraphics: GuiGraphics,
    mouseX: Int,
    mouseY: Int,
    f: Float
  ) {
    guiGraphics.blitSprite(TEXTURE, 0, 0, w, h)
  }

  companion object {
    val TEXTURE = Unfocused.location("container/background")
  }
}