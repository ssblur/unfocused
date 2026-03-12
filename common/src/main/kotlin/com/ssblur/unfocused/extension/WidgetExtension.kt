package com.ssblur.unfocused.extension

import com.ssblur.unfocused.screen.widget.PositionedWidget
import net.minecraft.client.gui.GuiGraphics

object WidgetExtension {
  fun Iterable<PositionedWidget>.renderAll(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    for(widget in this) {
      widget.render(guiGraphics, i, j, f)
    }
  }
}