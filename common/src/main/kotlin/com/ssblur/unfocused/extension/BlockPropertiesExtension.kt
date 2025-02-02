package com.ssblur.unfocused.extension

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockState

@Suppress("unused")
object BlockPropertiesExtension {
  private val SPAWN_NEVER = { a: BlockState?, b: BlockGetter?, c: BlockPos?, d: EntityType<*>? -> false }
  private val NEVER = { a: BlockState?, b: BlockGetter?, c: BlockPos? -> false }

  fun Properties.isNeverAValidSpawn(): Properties {
    this.isValidSpawn(SPAWN_NEVER)
    return this
  }

  fun Properties.isNeverSuffocating(): Properties {
    this.isSuffocating(NEVER)
    return this
  }

  fun Properties.isNeverViewBlocking(): Properties {
    this.isViewBlocking(NEVER)
    return this
  }

  fun Properties.isNeverRedstoneConductor(): Properties {
    this.isRedstoneConductor(NEVER)
    return this
  }

  fun Properties.isEphemeral(): Properties =
    this.isNeverSuffocating()
      .isNeverAValidSpawn()
      .isNeverRedstoneConductor()
      .isNeverViewBlocking()
}