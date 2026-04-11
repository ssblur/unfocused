package com.ssblur.unfocused.config

import com.ssblur.unfocused.ModInitializer
import net.minecraft.world.level.gamerules.GameRule

@Suppress("unused")
class GameRuleConfig(val mod: ModInitializer) {
  fun registerBoolean(name: String, default: Boolean): () -> Boolean {
    val configured = mod.CONFIG.get(name, if (default) "true" else "false").toBooleanStrict()
//    val rule = register("${mod.id}:$name", GameRules.Category.MISC, GameRules.BooleanValue.create(configured)) TODO
//    BOOL_RULES[rule] = configured
//    return { BOOL_RULES[rule] ?: configured }
    return { default }
  }

  fun registerInt(name: String, default: Int): () -> Int {
    val configured = mod.CONFIG.get(name, default.toString()).toInt()
//    val rule = register("${mod.id}:$name", GameRules.Category.MISC, GameRules.IntegerValue.create(configured)) TODO
//    INT_RULES[rule] = configured
//    return { INT_RULES[rule] ?: configured }
    return { default }
  }

  companion object {
    val INT_RULES: MutableMap<GameRule<Int>, Int> = mutableMapOf()
    val BOOL_RULES: MutableMap<GameRule<Boolean>, Boolean> = mutableMapOf()
  }
}