package com.ssblur.unfocused.menu

import com.ssblur.unfocused.Unfocused
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class UnfocusedBookMenu(i: Int, val inventory: Inventory? = null): AbstractContainerMenu(Unfocused.BOOK_MENU.get(), i) {
  /***
   * Location of the markdown file to render for this menu, relative to the lang root for markdown files
   * e.g. assets/unfocused/unfocused/markdown/en_us/book.md would be unfocused:book
   * Root/fallback lang is en_us by default, but if a book is available in the current language it will be preferred
   */
  var location: ResourceLocation? // don't worry about how jank this is ender (:
    get() = slot.item[DataComponents.ITEM_NAME]?.string?.let { Unfocused.location(it) }
    set(value) {
      val item = ItemStack(Items.STICK)
      item[DataComponents.ITEM_NAME] = value?.let { Component.literal(it.toString()) }
      slot.set(item)
    }
  private val container = SimpleContainer(1)
  private val slot: Slot = addSlot(Slot(container, 0, Integer.MAX_VALUE, Integer.MAX_VALUE))
  override fun quickMoveStack(player: Player, i: Int): ItemStack = ItemStack.EMPTY
  override fun stillValid(player: Player) = true
}