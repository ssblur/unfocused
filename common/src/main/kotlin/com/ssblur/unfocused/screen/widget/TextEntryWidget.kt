package com.ssblur.unfocused.screen.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarrationElementOutput

class TextEntryWidget(x: Int, y: Int, w: Int, h: Int, scissor: Boolean = true) : PositionedWidget(x, y, w, h, scissor) {
  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    TODO("Not yet implemented")
  }

  override fun draw(
    guiGraphics: GuiGraphics,
    mouseX: Int,
    mouseY: Int,
    f: Float
  ) {
    TODO("Not yet implemented")
  }

  override fun keyPressed(i: Int, j: Int, k: Int): Boolean {
    return super.keyPressed(i, j, k)
  }

  override fun keyReleased(i: Int, j: Int, k: Int): Boolean {
    return super.keyReleased(i, j, k)
  }

  override fun charTyped(c: Char, i: Int): Boolean {
    return super.charTyped(c, i)
  }
}