package com.ssblur.unfocused.config

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.serialization.Codec
import com.ssblur.unfocused.ModInitializer
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.level.gamerules.GameRule
import net.minecraft.world.level.gamerules.GameRuleCategory
import net.minecraft.world.level.gamerules.GameRuleType
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor

@Suppress("unused")
class GameRuleConfig(val mod: ModInitializer) {
  val categories: MutableMap<String, GameRuleCategory> = mutableMapOf()
  fun registerBoolean(name: String, default: Boolean, category: String = "default"): () -> Boolean {
    val configured = mod.CONFIG.get(name, if (default) "true" else "false").toBooleanStrict()
    categories[category] = categories[category] ?: GameRuleCategory.register(mod.location(category))
    val rule = GameRule(
      categories[category]!!,
      GameRuleType.BOOL,
      BoolArgumentType.bool(),
      GameRuleTypeVisitor::visitBoolean,
      Codec.BOOL,
      { if(it) 1 else 0 },
      configured,
      FeatureFlagSet.of()
    )
    BOOL_RULES[rule] = configured
    mod.GAMERULES.register(name) {
      rule
    }
    return { BOOL_RULES[rule] ?: configured }
  }

  fun registerInt(name: String, default: Int, category: String = "default"): () -> Int {
    val configured = mod.CONFIG.get(name, default.toString()).toInt()
    categories[category] = categories[category] ?: GameRuleCategory.register(mod.location(category))
    val rule = GameRule(
      categories[category]!!,
      GameRuleType.INT,
      IntegerArgumentType.integer(),
      GameRuleTypeVisitor::visitInteger,
      Codec.INT,
      { it },
      configured,
      FeatureFlagSet.of()
    )
    mod.GAMERULES.register(name) {
      rule
    }
    INT_RULES[rule] = configured
    return { INT_RULES[rule] ?: configured }
  }

  companion object {
    val INT_RULES: MutableMap<GameRule<Int>, Int> = mutableMapOf()
    val BOOL_RULES: MutableMap<GameRule<Boolean>, Boolean> = mutableMapOf()
  }
}