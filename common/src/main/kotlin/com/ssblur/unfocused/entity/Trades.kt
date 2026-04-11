package com.ssblur.unfocused.entity

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.npc.villager.VillagerProfession
import net.minecraft.world.entity.npc.villager.VillagerTrades

@Suppress("unused", "unusedreceiverparameter")
object Trades: SimpleEvent<Trades.Trade>(retroactive = true, clearAfterRun = false) {
  data class Trade(val profession: ResourceKey<VillagerProfession>?, val rarity: Int, val trade: VillagerTrades.ItemListing)

  fun ModInitializer.registerVillagerTrade(
    profession: ResourceKey<VillagerProfession>,
    level: Int,
    trade: VillagerTrades.ItemListing
  ): VillagerTrades.ItemListing {
    callback(Trade(profession, level, trade))
    return trade
  }

  fun ModInitializer.registerWanderingTraderTrade(rarity: Int, trade: VillagerTrades.ItemListing): VillagerTrades.ItemListing {
    callback(Trade(null, rarity, trade))
    return trade
  }
}