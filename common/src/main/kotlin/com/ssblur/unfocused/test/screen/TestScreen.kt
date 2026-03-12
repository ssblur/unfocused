package com.ssblur.unfocused.test.screen

import com.ssblur.unfocused.screen.UnfocusedScreen
import com.ssblur.unfocused.screen.widget.ButtonWidget
import com.ssblur.unfocused.screen.widget.MarkdownWidget
import com.ssblur.unfocused.test.menu.TestMenu
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class TestScreen(abstractContainerMenu: TestMenu, inventory: Inventory, component: Component) :
  UnfocusedScreen<TestMenu>(abstractContainerMenu, inventory, component) {
  init {
    build()
  }

  override fun rebuildWidgets() {
    super.rebuildWidgets()
    build()
  }

  fun build() {
    add(MarkdownWidget(
      15,
      15,
      400,
      100,
      "**test**: true.\n\nI can write a variety of [things](house) and they should be formatted *relatively* well " +
          "here.![unfocused icon](unfocused:icon.png)\n\nI can even include\n\n# Multiple lines\n\n~~you know, if I wanted " +
          "to~~\n\n[External Link](https://blur.gay)"
    )).setColor(255, 200, 200)

    add(ButtonWidget(15, 100, 120, 24, Component.literal("close")) {
      Minecraft.getInstance().player!!.closeContainer()
    })

    add(ButtonWidget(140, 100, 120, 24, Component.literal("close 2")) {
      Minecraft.getInstance().player!!.closeContainer()
    })
  }
}