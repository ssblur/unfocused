package com.ssblur.unfocused.screen

import com.ssblur.unfocused.screen.widget.PositionedWidget
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

abstract class UnfocusedScreen<T : AbstractContainerMenu>(
  abstractContainerMenu: T,
  inventory: Inventory,
  component: Component,
  width: Int = 0,
  height: Int = 0,
) :
  AbstractContainerScreen<T>(abstractContainerMenu, inventory, component, width, height) {
  var widgets: MutableList<PositionedWidget> = mutableListOf()
  fun <T: Renderable> add(guiEventListener: T): T {
    if(guiEventListener is NarratableEntry && guiEventListener is GuiEventListener) {
      val widget = super.addRenderableWidget(guiEventListener)
      if(widget is PositionedWidget) {
        widget.parent = this
        widgets.add(widget)
      }
      return widget
    }
    return addRenderableOnly(guiEventListener)
  }

  override fun clearWidgets() {
    super.clearWidgets()
    widgets = mutableListOf()
  }

  override fun extractRenderState(guiGraphics: GuiGraphicsExtractor, i: Int, j: Int, f: Float) {
    super.extractRenderState(guiGraphics, i, j, f)
    extractTooltip(guiGraphics, i, j)
  }

  override fun extractContents(guiGraphics: GuiGraphicsExtractor, i: Int, j: Int, f: Float) {
    super.extractContents(guiGraphics, i, j, f)
  }

  override fun mouseClicked(event: MouseButtonEvent, bl: Boolean): Boolean {
    return super.mouseClicked(event, bl)
  }

  override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
    widgets.forEach { if(it.mouseScrolled(d, e, f, g)) return true }
    return super.mouseScrolled(d, e, f, g)
  }

  override fun extractBackground(guiGraphics: GuiGraphicsExtractor, j: Int, mouseY: Int, f: Float) {
    extractBlurredBackground(guiGraphics)
  }

  override fun isInGameUi(): Boolean = true
  override fun isPauseScreen(): Boolean = false
  override fun canInterruptWithAnotherScreen(): Boolean = true
  override fun isAllowedInPortal(): Boolean = false

  override fun extractLabels(guiGraphics: GuiGraphicsExtractor, i: Int, j: Int) {}
}