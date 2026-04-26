package com.ssblur.unfocused.event.client

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor

object ClientGuiRenderEvent: SimpleEvent<ClientGuiRenderEvent.GuiRenderEvent>(false) {
  data class GuiRenderEvent(val guiGraphics: GuiGraphicsExtractor, val deltaTime: DeltaTracker)
}