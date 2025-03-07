package com.ssblur.unfocused

import com.ssblur.unfocused.config.GameRuleConfig
import com.ssblur.unfocused.event.common.ServerStartEvent
import org.apache.logging.log4j.LogManager

object Unfocused: ModInitializer("unfocused") {
  var isNeoForge = false
  var isFabric = false
  val LOGGER = LogManager.getLogger(id)!!

  fun init() {
    ServerStartEvent.register { server ->
      server.addTickable {
        if (server.tickCount % 20 == 0) {
          GameRuleConfig.BOOL_RULES.map {
            GameRuleConfig.BOOL_RULES[it.key] = server.gameRules.getRule(it.key).get()
          }
          GameRuleConfig.INT_RULES.map {
            GameRuleConfig.INT_RULES[it.key] = server.gameRules.getRule(it.key).get()
          }
        }
      }
    }
  }

  fun isModLoaded(id: String) = UtilityExpectPlatform.isModLoaded(id)
}