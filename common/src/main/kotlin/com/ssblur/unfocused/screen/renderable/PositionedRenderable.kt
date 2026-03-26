package com.ssblur.unfocused.screen.renderable

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.screens.Screen
import kotlin.math.roundToInt

abstract class PositionedRenderable(
  var x: Int,
  var y: Int,
  var w: Int,
  var h: Int,
  var scissor: Boolean = true,
  ): Renderable {
  var parent: Screen? = null
  var scroll = 0.0
  var maxScroll = h
  fun scroll(difference: Double) {
    scroll += difference
    scroll = scroll.coerceIn(0.0..(maxScroll - h).toDouble())
  }

  var hovered: Boolean = false
    private set

  override fun render(
    guiGraphics: GuiGraphics,
    i: Int,
    j: Int,
    f: Float
  ) {
    hovered = mouseOver(i.toDouble(), j.toDouble())

    val stack = guiGraphics.pose()
    stack.pushPose()
    // Scissor by default so that I don't have to worry about overdraw in widgets.
    if(scissor) guiGraphics.enableScissor(x, y, w+x, h+y)
    // Translate the stack so that any draws can be made relative to the widget's position.
    // (I'm lazy and want to be able to dynamically nest without doing math)
    stack.translate(x.toFloat(), y.toFloat() - scroll.toFloat(), 0.0f)

    this.draw(guiGraphics, i - x, j - (y - scroll.roundToInt()), f)

    if(scissor) guiGraphics.disableScissor()

    if(maxScroll > h) {
      val barSize = h.toDouble() / maxScroll // as a proportion of widget height
      val barProgress = (scroll / (maxScroll - h))
      val barPos = barProgress * (1.0 - barSize)

      val barH = (barSize * h).toInt()
      val barY = (barPos * h).toInt() + scroll.toInt()
      val barW = 2
      val barX = w - barW

      RenderSystem.enableBlend()
      guiGraphics.fill(barX, barY, barX + barW, barY + barH, 0x44000000u.toInt())
    }

    this.drawOverlay(guiGraphics, i - x, j - (y - scroll.roundToInt()), f)

    stack.popPose()
  }

  fun mouseOver(d: Double, e: Double): Boolean {
    return d.roundToInt() in x..x + w && e.roundToInt() in y..y + h
  }

  abstract fun draw(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float)
  open fun drawOverlay(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float) {}

  companion object {

  }
}