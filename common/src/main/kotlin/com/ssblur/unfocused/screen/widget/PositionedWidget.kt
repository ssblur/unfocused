package com.ssblur.unfocused.screen.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.Screen
import kotlin.math.roundToInt

abstract class PositionedWidget(
  var x: Int,
  var y: Int,
  var w: Int,
  var h: Int,
  var scissor: Boolean = true,
): NarratableEntry, Renderable, GuiEventListener {
  var parent: Screen? = null
  var scroll = 0.0
  var maxScroll = h
  fun scroll(difference: Double) {
    scroll += difference
    scroll = scroll.coerceIn(0.0..(maxScroll - h).toDouble())
  }

  override fun narrationPriority() = NarratableEntry.NarrationPriority.HOVERED

  var hovered: Boolean = false
    private set

  override fun render(
    guiGraphics: GuiGraphics,
    i: Int,
    j: Int,
    f: Float
  ) {
    hovered = isMouseOver(i.toDouble(), j.toDouble())

    val stack = guiGraphics.pose()
    stack.pushPose()
    // Scissor by default so that I don't have to worry about overdraw in widgets.
    if(scissor) guiGraphics.enableScissor(x, y, w, h)
    // Translate the stack so that any draws can be made relative to the widget's position.
    // (I'm lazy and want to be able to dynamically nest without doing math)
    stack.translate(x.toFloat(), y.toFloat() - scroll.toFloat(), 0.0f)

    this.draw(guiGraphics, i - x, j - (y - scroll.roundToInt()), f)

    if(scissor) guiGraphics.disableScissor()

    this.drawOverlay(guiGraphics, i - x, j - (y - scroll.roundToInt()), f)

    stack.popPose()
  }

  abstract fun draw(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float)
  open fun drawOverlay(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float) {}

  private var focused: Boolean = false
  override fun setFocused(bl: Boolean) {
    focused = bl
  }
  override fun isFocused(): Boolean = focused

  override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
    if(!isMouseOver(d, e)) return false
    if(i == 0) return leftClick(d, e)
    if(i == 1) return rightClick(d, e)
    return super.mouseClicked(d, e, i)
  }

  override fun isMouseOver(d: Double, e: Double): Boolean {
    return d > x && d < x + w && e > y && e < y + h
  }

  open fun leftClick(x: Double, y: Double) = false
  open fun rightClick(x: Double, y: Double) = false

  override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
    if(!isMouseOver(d, e)) return false
    if(maxScroll > h) scroll(g * -3.0)
    return true
  }
}