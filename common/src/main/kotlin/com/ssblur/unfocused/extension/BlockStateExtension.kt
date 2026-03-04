package com.ssblur.unfocused.extension

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Predicate

@Suppress("unused")
object BlockStateExtension {
  infix fun BlockState.matches(block: Block): Boolean {
    return this.`is`(block)
  }

  infix fun BlockState.matches(block: ResourceKey<Block>): Boolean {
    return this.`is`(block)
  }

  infix fun BlockState.matches(block: Holder<Block>): Boolean {
    return this.`is`(block)
  }

  infix fun BlockState.matches(blocks: HolderSet<Block>): Boolean {
    return this.`is`(blocks)
  }

  infix fun BlockState.matches(tag: TagKey<Block>): Boolean {
    return this.`is`(tag)
  }

  fun BlockState.matches(block: TagKey<Block>, predicate: Predicate<BlockBehaviour.BlockStateBase>): Boolean {
    return this.`is`(block, predicate)
  }
}