package com.ssblur.unfocused.screen.renderable

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import kotlin.math.roundToInt

@Suppress("unused")
class VerticalProgressGraphic(
  val background: ResourceLocation,
  val foreground: ResourceLocation,
  x: Int, y: Int, w: Int, h: Int,
  /**
   * if true, this fills top-down
   * if false, it fills bottom-up
   */
  var topDown: Boolean = true
):
  PositionedRenderable(x, y, w, h, true) {
  /**
   * progress, from 0-1
   */
  var progress = 0.0
  override fun draw(
    guiGraphics: GuiGraphics,
    mouseX: Int,
    mouseY: Int,
    f: Float
  ) {
    draw(guiGraphics, 0, 0, w, h, background, foreground, progress, topDown)
  }

  companion object {
    fun draw(
      guiGraphics: GuiGraphics,
      x: Int,
      y: Int,
      width: Int,
      height: Int,
      background: ResourceLocation,
      foreground: ResourceLocation,
      progress: Double,
      topDown: Boolean
    ) {
      guiGraphics.blitSprite(background, x, y, width, height)
      if(topDown) guiGraphics.enableScissor(x, y, x + width, y + (height*progress).roundToInt())
      else guiGraphics.enableScissor(x, y + (height*(1.0-progress)).roundToInt(), x + width, y + height)
      guiGraphics.blitSprite(foreground, x, y, width, height)
      guiGraphics.disableScissor()
    }
  }
}