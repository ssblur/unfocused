package com.ssblur.unfocused.test.menu

import com.ssblur.unfocused.test.UnfocusedTestMod
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class TestMenu(i: Int, val inventory: Inventory? = null) : AbstractContainerMenu(UnfocusedTestMod.MENU.get(), i) {
  override fun quickMoveStack(
    player: Player,
    i: Int
  ): ItemStack {
    return ItemStack.EMPTY
  }

  override fun stillValid(player: Player): Boolean = true
}