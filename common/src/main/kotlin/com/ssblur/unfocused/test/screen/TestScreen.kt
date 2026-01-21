package com.ssblur.unfocused.test.screen

import com.ssblur.unfocused.test.menu.TestMenu
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class TestScreen(abstractContainerMenu: TestMenu, inventory: Inventory, component: Component) :
  AbstractContainerScreen<TestMenu>(abstractContainerMenu, inventory, component) {
  override fun renderBg(
    guiGraphics: GuiGraphics,
    f: Float,
    i: Int,
    j: Int
  ) {

  }

  override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
//    super.render(guiGraphics, i, j, f)
  }
}