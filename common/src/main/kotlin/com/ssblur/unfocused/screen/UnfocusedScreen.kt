package com.ssblur.unfocused.screen

import com.ssblur.unfocused.extension.WidgetExtension.renderAll
import com.ssblur.unfocused.screen.widget.PositionedWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

abstract class UnfocusedScreen<T : AbstractContainerMenu>(abstractContainerMenu: T, inventory: Inventory, component: Component) :
  AbstractContainerScreen<T>(abstractContainerMenu, inventory, component) {
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

  fun renderWidgets(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    widgets.renderAll(guiGraphics, i, j, f)
  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)
    renderWidgets(guiGraphics, i, j, f)
  }

  override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
    return super.mouseClicked(d, e, i)
  }

  override fun renderBg(
    guiGraphics: GuiGraphics,
    f: Float,
    i: Int,
    j: Int
  ) {}

  override fun renderLabels(guiGraphics: GuiGraphics, i: Int, j: Int) {}
}