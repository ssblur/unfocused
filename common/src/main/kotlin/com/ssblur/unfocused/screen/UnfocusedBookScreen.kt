package com.ssblur.unfocused.screen

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.helper.LocalizedMarkdownReader
import com.ssblur.unfocused.menu.UnfocusedBookMenu
import com.ssblur.unfocused.screen.renderable.SinglePageBackground
import com.ssblur.unfocused.screen.widget.ButtonWidget
import com.ssblur.unfocused.screen.widget.MarkdownWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class UnfocusedBookScreen(val bookMenu: UnfocusedBookMenu, inventory: Inventory, component: Component) :
  UnfocusedScreen<UnfocusedBookMenu>(bookMenu, inventory, component) {
  var initialized = false

  override fun init() {
    add(SinglePageBackground(80, 10, 265, 220))

    bookMenu.location?.also {
      initialized = true
      add(MarkdownWidget(
        100,
        30,
        220,
        145,
        LocalizedMarkdownReader.read(it),
        false
      )).setColor(0, 0, 0)
    } ?: run {
      add(MarkdownWidget(
        100,
        30,
        220,
        145,
        LocalizedMarkdownReader.read(Unfocused.location("loading")),
        false
      )).setColor(0, 0, 0)
    }

    add(ButtonWidget(260, 180, 65, 24, Component.translatable("extra.unfocused.close")) {
      Minecraft.getInstance().player?.closeContainer()
    })
  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)
    if(!initialized && bookMenu.location != null) {
      rebuildWidgets()
    }
  }
}