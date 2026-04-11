package com.ssblur.unfocused.constructors

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

@Suppress("unused")
class Effect(mobEffectCategory: MobEffectCategory, color: Int): MobEffect(mobEffectCategory, color) {
  fun interface EffectApplicator {
    fun apply(livingEntity: LivingEntity)
  }

  var function: EffectApplicator? = null

  constructor(
    mobEffectCategory: MobEffectCategory = MobEffectCategory.NEUTRAL,
    color: Int = 0x0,
    function: EffectApplicator
  ): this(mobEffectCategory, color) {
    this.function = function
  }

  override fun applyEffectTick(serverLevel: ServerLevel, livingEntity: LivingEntity, i: Int): Boolean {
    function?.apply(livingEntity)
    return true
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean {
    return function != null
  }
}