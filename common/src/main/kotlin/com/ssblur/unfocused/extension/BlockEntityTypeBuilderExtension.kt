package com.ssblur.unfocused.extension

import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

@Suppress("unused")
object BlockEntityTypeBuilderExtension {
    fun <T: BlockEntity> BlockEntityType.Builder<T>.build(): BlockEntityType<T> {
        return this.build(null)
    }
}