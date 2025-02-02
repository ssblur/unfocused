package com.ssblur.unfocused.event.client

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

object ClientLoreEvent: SimpleEvent<ClientLoreEvent.LoreContext>(false) {
  data class LoreContext(
    val stack: ItemStack,
    val lore: MutableList<Component>,
    val context: Item.TooltipContext,
    val flag: TooltipFlag
  )
}