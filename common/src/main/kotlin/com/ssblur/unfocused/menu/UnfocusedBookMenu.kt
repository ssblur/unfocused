package com.ssblur.unfocused.menu

import com.ssblur.unfocused.Unfocused
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class UnfocusedBookMenu(i: Int, val inventory: Inventory? = null): AbstractContainerMenu(Unfocused.BOOK_MENU.get(), i) {
  /***
   * Location of the markdown file to render for this menu, relative to the lang root for markdown files
   * e.g. assets/unfocused/unfocused/markdown/en_us/book.md would be unfocused:book
   * Root/fallback lang is en_us by default, but if a book is available in the current language it will be preferred
   */
  var location: ResourceLocation? = null
  override fun quickMoveStack(player: Player, i: Int) = ItemStack.EMPTY
  override fun stillValid(player: Player) = true
}