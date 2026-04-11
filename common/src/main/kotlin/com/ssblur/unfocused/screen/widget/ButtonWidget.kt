package com.ssblur.unfocused.screen.widget

import com.ssblur.unfocused.Unfocused.location
import com.ssblur.unfocused.extension.SoundEventExtension.play
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents

class ButtonWidget(x: Int, y: Int, w: Int, h: Int, val label: Component, val onClick: () -> Unit) :
  PositionedWidget(x, y, w, h, scissor = false) {

  override fun draw(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float) {
    if(disabled)
      guiGraphics.blitSprite(RenderPipelines.GUI, BUTTON_DISABLED_TEXTURE, 0, 0, w, h)
    else if(hovered)
      guiGraphics.blitSprite(RenderPipelines.GUI, BUTTON_HIGHLIGHTED_TEXTURE, 0, 0, w, h)
    else
      guiGraphics.blitSprite(RenderPipelines.GUI, BUTTON_TEXTURE, 0, 0, w, h)

    val font = Minecraft.getInstance().font
    val lx = w / 2 - font.width(label) / 2
    val ly = h/2 - font.lineHeight/2
    guiGraphics.drawString(font, label, lx, ly, 0xffffffffu.toInt())
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("extra.unfocused.button", label))
  }

  override fun leftClick(x: Double, y: Double): Boolean {
    if(!disabled) {
      SoundEvents.UI_BUTTON_CLICK.play()
      onClick()
      return true
    }
    return super.leftClick(x, y)
  }

  var disabled: Boolean = false

  companion object {
    val BUTTON_TEXTURE = location("minecraft:widget/button")
    val BUTTON_DISABLED_TEXTURE = location("minecraft:widget/button_disabled")
    val BUTTON_HIGHLIGHTED_TEXTURE = location("minecraft:widget/button_highlighted")
  }
}