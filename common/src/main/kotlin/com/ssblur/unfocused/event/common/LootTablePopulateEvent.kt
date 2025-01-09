package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootTable

object LootTablePopulateEvent: SimpleEvent<LootTablePopulateEvent.LootTableContext>(retroactive = true, cancelable = false) {
    data class LootTableContext(val id: ResourceKey<LootTable>, val isBuiltin: Boolean, val builder: LootTable)
}