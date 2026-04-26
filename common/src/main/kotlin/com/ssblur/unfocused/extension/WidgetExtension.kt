package com.ssblur.unfocused.extension

import com.ssblur.unfocused.screen.widget.PositionedWidget
import net.minecraft.client.gui.GuiGraphicsExtractor

object WidgetExtension {
  fun Iterable<PositionedWidget>.renderAll(guiGraphics: GuiGraphicsExtractor, i: Int, j: Int, f: Float) {
    for(widget in this) {
      widget.extractRenderState(guiGraphics, i, j, f)
    }
  }
}