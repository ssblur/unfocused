package com.ssblur.unfocused.screen.widget

import com.ssblur.unfocused.Unfocused.location
import com.ssblur.unfocused.extension.SoundEventExtension.play
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents

@Suppress("unused")
class HelpButtonWidget(x: Int, y: Int, w: Int, h: Int, val location: ResourceLocation) :
  PositionedWidget(x, y, w, h, scissor = false) {

  override fun draw(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float) {
    if(disabled)
      guiGraphics.blitSprite(BUTTON_DISABLED_TEXTURE, 0, 0, w, h)
    else if(hovered)
      guiGraphics.blitSprite(BUTTON_HIGHLIGHTED_TEXTURE, 0, 0, w, h)
    else
      guiGraphics.blitSprite(BUTTON_TEXTURE, 0, 0, w, h)

    val label = "?"
    val font = Minecraft.getInstance().font
    val lx = w / 2 - font.width(label) / 2
    val ly = h/2 - font.lineHeight/2
    guiGraphics.drawString(font, label, lx, ly, 0xffffffffu.toInt())
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("extra.unfocused.help_button"))
  }

  override fun leftClick(x: Double, y: Double): Boolean {
    if(!disabled) {
      SoundEvents.UI_BUTTON_CLICK.play()

      // open book screen
      Minecraft.getInstance().player?.connection?.sendUnsignedCommand("unfocused open $location")

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