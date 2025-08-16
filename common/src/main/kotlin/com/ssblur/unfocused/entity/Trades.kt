package com.ssblur.unfocused.entity

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing

@Suppress("unused", "unusedreceiverparameter")
object Trades: SimpleEvent<Trades.Trade>(retroactive = true, clearAfterRun = false) {
  data class Trade(val profession: ResourceKey<VillagerProfession>?, val rarity: Int, val trade: ItemListing)

  fun ModInitializer.registerVillagerTrade(
    profession: ResourceKey<VillagerProfession>,
    level: Int,
    trade: ItemListing
  ): ItemListing {
    callback(Trade(profession, level, trade))
    return trade
  }

  fun ModInitializer.registerWanderingTraderTrade(rarity: Int, trade: ItemListing): ItemListing {
    callback(Trade(null, rarity, trade))
    return trade
  }
}