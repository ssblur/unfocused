package com.ssblur.unfocused.screen

import com.ssblur.unfocused.extension.WidgetExtension.renderAll
import com.ssblur.unfocused.screen.renderable.PositionedRenderable
import com.ssblur.unfocused.screen.renderable.SlotBackground.Companion.TEXTURE
import com.ssblur.unfocused.screen.widget.PositionedWidget
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot

@Suppress("unused")
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

  var backgroundWidgets: MutableList<PositionedRenderable> = mutableListOf()
  fun <T: Renderable> addBackground(guiEventListener: T): T {
    if(guiEventListener is PositionedRenderable) {
      guiEventListener.parent = this
      backgroundWidgets.add(guiEventListener)
    }
    if(guiEventListener is NarratableEntry && guiEventListener is GuiEventListener) {
      val widget = super.addWidget(guiEventListener)
      return widget
    }
    return guiEventListener
  }

  override fun clearWidgets() {
    super.clearWidgets()
    widgets = mutableListOf()
    backgroundWidgets = mutableListOf()
  }

  fun renderWidgets(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    widgets.renderAll(guiGraphics, i, j, f)
  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)
//    renderWidgets(guiGraphics, i, j, f)
    renderTooltip(guiGraphics, i, j)
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

  override fun renderBackground(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.renderBackground(guiGraphics, i, j, f)
    backgroundWidgets.forEach {
      it.render(guiGraphics, i, j, f)
    }
  }
  override fun renderLabels(guiGraphics: GuiGraphics, i: Int, j: Int) {}

  fun renderSlotBackground(guiGraphics: GuiGraphics, slot: Slot) {
    val x = slot.x - 1
    val y = slot.y - 1
    guiGraphics.blitSprite(TEXTURE, x + leftPos, y + topPos, 18, 18)
  }

  fun renderSlotBackgrounds(guiGraphics: GuiGraphics, vararg slots: Slot) {
    slots.forEach {
      renderSlotBackground(guiGraphics, it)
    }
  }
}