package com.ssblur.unfocused.menu

import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu

class SimpleMenuProvider(
  val name: Component = Component.literal(""),
  val factory: (i: Int, inventory: Inventory, player: Player) -> AbstractContainerMenu
): MenuProvider {
  override fun getDisplayName(): Component = name

  override fun createMenu(
    i: Int,
    inventory: Inventory,
    player: Player
  ): AbstractContainerMenu = factory(i, inventory, player)
}