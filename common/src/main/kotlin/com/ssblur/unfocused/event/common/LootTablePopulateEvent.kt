package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import com.ssblur.unfocused.mixin.LootTableAccessor
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable

@Suppress("unused")
object LootTablePopulateEvent: SimpleEvent<LootTablePopulateEvent.LootTableContext>(retroactive = true, cancelable = false) {
    data class LootTableContext(val id: ResourceKey<LootTable>, val isBuiltin: Boolean, val pool: MutableList<LootPool.Builder>) {
        var pools: List<LootPool>
            get() = (pool as LootTableAccessor).pools
            set(pools) {
                (pool as LootTableAccessor).pools = pools
            }
    }
}