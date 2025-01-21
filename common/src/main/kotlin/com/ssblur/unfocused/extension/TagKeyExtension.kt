package com.ssblur.unfocused.extension

import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

@Suppress("unused")
object TagKeyExtension {
    operator fun TagKey<Block>.contains(state: BlockState): Boolean {
        return state.`is`(this)
    }

    operator fun TagKey<Item>.contains(item: ItemStack): Boolean {
        return item.`is`(this)
    }
}