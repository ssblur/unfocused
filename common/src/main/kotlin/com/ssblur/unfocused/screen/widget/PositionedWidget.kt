package com.ssblur.unfocused.screen.widget

import com.ssblur.unfocused.screen.renderable.PositionedRenderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.input.MouseButtonEvent

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

  override fun mouseClicked(event: MouseButtonEvent, bl: Boolean): Boolean {
    val mx = event.x
    val my = event.y
    val button = event.button()
    if(!isMouseOver(mx, my)) return false
    if(button == 0) return leftClick(mx - x, my - y + scroll)
    if(button == 1) return rightClick(mx - x, my - y + scroll)
    return super.mouseClicked(event, bl)
  }

  override fun isMouseOver(x: Double, y: Double) = mouseOver(x,y)

  open fun leftClick(x: Double, y: Double) = false
  open fun rightClick(x: Double, y: Double) = false

  override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
    if(!isMouseOver(d, e)) return false
    if(maxScroll > h) scroll(g * -6.0)
    return true
  }
}