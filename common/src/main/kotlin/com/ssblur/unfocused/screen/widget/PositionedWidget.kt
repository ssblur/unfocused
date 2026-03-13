package com.ssblur.unfocused.screen.widget

import com.ssblur.unfocused.screen.renderable.PositionedRenderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry

abstract class PositionedWidget(
  x: Int,
  y: Int,
  w: Int,
  h: Int,
  scissor: Boolean = true,
): NarratableEntry, GuiEventListener, PositionedRenderable(x, y, w, h, scissor) {
  override fun narrationPriority() = NarratableEntry.NarrationPriority.HOVERED

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

  override fun isMouseOver(d: Double, e: Double) = mouseOver(d, e)

  open fun leftClick(x: Double, y: Double) = false
  open fun rightClick(x: Double, y: Double) = false

  override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
    if(!isMouseOver(d, e)) return false
    if(maxScroll > h) scroll(g * -6.0)
    return true
  }
}