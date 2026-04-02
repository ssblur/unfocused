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
import java.io.FileNotFoundException

class UnfocusedBookScreen(val bookMenu: UnfocusedBookMenu, inventory: Inventory, component: Component) :
  UnfocusedScreen<UnfocusedBookMenu>(bookMenu, inventory, component) {
  var initialized = false

  override fun init() {
    imageWidth = 265
    imageHeight = 220
    leftPos = (this.width - imageWidth) / 2
    topPos = (this.height - imageHeight) / 2
    add(SinglePageBackground(leftPos, topPos, imageWidth, imageHeight))

    try {
      bookMenu.location?.also {
        initialized = true
        add(MarkdownWidget(
          leftPos + 20,
          topPos + 15,
          220,
          imageHeight - 62,
          LocalizedMarkdownReader.read(it),
          false,
          commandsAllowed = false
        )).setColor(0, 0, 0)
      } ?: run {
        add(
          MarkdownWidget(
            leftPos + 20,
            topPos + 15,
            220,
            imageHeight - 62,
            LocalizedMarkdownReader.read(Unfocused.location("loading")),
            shadow = false,
            commandsAllowed = false
          )
        ).setColor(0, 0, 0)
      }
    } catch(e: FileNotFoundException) {
      add(
        MarkdownWidget(
          leftPos + 20,
          topPos + 15,
          220,
          imageHeight - 62,
          "Could not load! \n${e.message}",
          shadow = false,
          commandsAllowed = false
        )
      ).setColor(255, 0, 0)
    }

    add(ButtonWidget(leftPos + imageWidth - 85, topPos + imageHeight - 45, 65, 24, Component.translatable("extra.unfocused.close")) {
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