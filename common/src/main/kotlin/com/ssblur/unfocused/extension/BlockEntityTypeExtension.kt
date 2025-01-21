package com.ssblur.unfocused.extension

import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

@Suppress("unused")
object BlockEntityTypeExtension {
    operator fun <T: BlockEntity> BlockEntityType<T>.get(getter: BlockGetter, pos: BlockPos): T? {
        return this.getBlockEntity(getter, pos)
    }

    operator fun <T: BlockEntity> RegistrySupplier<BlockEntityType<T>>.get(getter: BlockGetter, pos: BlockPos): T? {
        return this.get()[getter, pos]
    }

    fun <T: BlockEntity> RegistrySupplier<BlockEntityType<T>>.create(pos: BlockPos, state: BlockState): T? {
        return this.get().create(pos, state)
    }
}