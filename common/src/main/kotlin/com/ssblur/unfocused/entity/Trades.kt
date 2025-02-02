package com.ssblur.unfocused.entity

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing

@Suppress("unused", "unusedreceiverparameter")
object Trades: SimpleEvent<Trades.Trade>(retroactive = true, clearAfterRun = false) {
  data class Trade(val profession: VillagerProfession?, val rarity: Int, val trade: ItemListing)

  fun ModInitializer.registerVillagerTrade(
    profession: VillagerProfession,
    level: Int,
    trade: ItemListing
  ): ItemListing {
    Trades.callback(Trade(profession, level, trade))
    return trade
  }

  fun ModInitializer.registerWanderingTraderTrade(rarity: Int, trade: ItemListing): ItemListing {
    Trades.callback(Trade(null, rarity, trade))
    return trade
  }
}