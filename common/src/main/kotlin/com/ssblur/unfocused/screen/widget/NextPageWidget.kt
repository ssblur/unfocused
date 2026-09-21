package com.ssblur.unfocused.screen.widget

import com.ssblur.unfocused.extension.SoundEventExtension.play
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents

@Suppress("unused")
class NextPageWidget(x: Int, y: Int, val onClick: () -> Unit) :
  PositionedWidget(x, y, 23, 13, scissor = false) {

  override fun draw(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float) {
    if(hovered)
      guiGraphics.blitSprite(NEXT_BUTTON_HIGHLIGHT, 0, 0, w, h)
    else
      guiGraphics.blitSprite(NEXT_BUTTON, 0, 0, w, h)
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("extra.unfocused.prev_page"))
  }

  override fun leftClick(x: Double, y: Double): Boolean {
    SoundEvents.UI_BUTTON_CLICK.play()
    onClick()
    return true
  }
}