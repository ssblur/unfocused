package com.ssblur.unfocused.config

import com.ssblur.unfocused.ModInitializer
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameRules.Key
import net.minecraft.world.level.GameRules.register

@Suppress("unused")
class GameRuleConfig(val mod: ModInitializer) {
    fun registerBoolean(name: String, default: Boolean): () -> Boolean {
        val configured = mod.CONFIG.get(name, if(default) "true" else "false").toBooleanStrict()
        val rule = register("${mod.id}:$name", GameRules.Category.MISC, GameRules.BooleanValue.create(configured))
        BOOL_RULES[rule] = configured
        return { BOOL_RULES[rule] ?: configured }
    }

    fun registerInt(name: String, default: Int): () -> Int {
        val configured = mod.CONFIG.get(name, default.toString()).toInt()
        val rule = register("${mod.id}:$name", GameRules.Category.MISC, GameRules.IntegerValue.create(configured))
        INT_RULES[rule] = configured
        return { INT_RULES[rule] ?: configured }
    }

    companion object {
        val INT_RULES: MutableMap<Key<GameRules.IntegerValue>, Int> = mutableMapOf()
        val BOOL_RULES: MutableMap<Key<GameRules.BooleanValue>, Boolean> = mutableMapOf()
    }
}