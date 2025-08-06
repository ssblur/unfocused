package com.ssblur.unfocused.screen.widget

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.narration.NarratableEntry

abstract class PositionedWidget(
  var x: Int,
  var y: Int,
  var w: Int,
  var h: Int,
  var scissor: Boolean = true
): NarratableEntry, Renderable {
  override fun narrationPriority() = NarratableEntry.NarrationPriority.HOVERED

  override fun render(
    guiGraphics: GuiGraphics,
    i: Int,
    j: Int,
    f: Float
  ) {
    val stack = guiGraphics.pose()
    stack.pushPose()
    // Translate the stack so that any draws can be made relative to the widget's position.
    // (I'm lazy and want to be able to dynamically nest without doing math)
    stack.translate(x.toFloat(), y.toFloat(), 0.0f)
    // Scissor by default so that I don't have to worry about overdraw in widgets.
    if(scissor) guiGraphics.enableScissor(0, 0, w, h)

    this.draw(guiGraphics)

    // If there is a way to check if scissor is enabled, I didn't see it.
    // So instead of reverting state, we just blindly disable scissor again :\
    guiGraphics.disableScissor()
    stack.popPose()
  }

  abstract fun draw(guiGraphics: GuiGraphics)
}