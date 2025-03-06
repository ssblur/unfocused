package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object PlayerCraftEvent: SimpleEvent<PlayerCraftEvent.PlayerCraftData>(false, false) {
  data class PlayerCraftData(val player: Player, val itemStack: ItemStack, val craftingContainer: Container)
}