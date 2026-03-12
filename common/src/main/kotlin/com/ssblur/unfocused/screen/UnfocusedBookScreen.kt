package com.ssblur.unfocused.screen

import com.ssblur.unfocused.helper.LocalizedMarkdownReader
import com.ssblur.unfocused.menu.UnfocusedBookMenu
import com.ssblur.unfocused.screen.widget.ButtonWidget
import com.ssblur.unfocused.screen.widget.MarkdownWidget
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class UnfocusedBookScreen(val bookMenu: UnfocusedBookMenu, inventory: Inventory, component: Component) :
  UnfocusedScreen<UnfocusedBookMenu>(bookMenu, inventory, component) {
  var initialized = false
  init {
    build()
  }

  override fun rebuildWidgets() {
    super.rebuildWidgets()
    build()
  }

  fun build() {
//    add(MarkdownWidget(
//      90,
//      20,
//      250,
//      175,
//      LocalizedMarkdownReader.read(Unfocused.location("landing"))
//    )).setColor(255, 255, 255)

    bookMenu.location?.let {
      initialized = true
      add(MarkdownWidget(
        90,
        20,
        250,
        175,
        LocalizedMarkdownReader.read(it)
      )).setColor(255, 255, 255)
    }

    add(ButtonWidget(275, 200, 65, 24, Component.literal("Close")) {
      Minecraft.getInstance().player!!.closeContainer()
    })
  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
    super.render(guiGraphics, i, j, f)
    if(!initialized && bookMenu.location != null) {
      build()
    }
  }
}