package com.ssblur.unfocused.screen.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component

class PlainTextWidget(val text: Component, x: Int, y: Int, w: Int, h: Int, scissor: Boolean = true, var color: UInt = 0x000000u):
  PositionedWidget(x, y, w, h, scissor) {
  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, text)
  }

  override fun draw(
    guiGraphics: GuiGraphics,
    mouseX: Int,
    mouseY: Int,
    f: Float
  ) {
    guiGraphics.drawWordWrap(Minecraft.getInstance().font, text, 0, 0, w, color.toInt())
  }
}