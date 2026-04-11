package com.ssblur.unfocused.helper

import com.ssblur.unfocused.config.GameRuleConfig
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.notifications.NotificationService
import net.minecraft.server.players.IpBanListEntry
import net.minecraft.server.players.NameAndId
import net.minecraft.server.players.ServerOpListEntry
import net.minecraft.server.players.UserBanListEntry
import net.minecraft.world.level.gamerules.GameRule

object UnfocusedNotificationService: NotificationService {
  override fun playerJoined(serverPlayer: ServerPlayer) {}

  override fun playerLeft(serverPlayer: ServerPlayer) {}

  override fun serverStarted() {}

  override fun serverShuttingDown() {}

  override fun serverSaveStarted() {}

  override fun serverSaveCompleted() {}

  override fun serverActivityOccured() {}

  override fun playerOped(serverOpListEntry: ServerOpListEntry) {}

  override fun playerDeoped(serverOpListEntry: ServerOpListEntry) {}

  override fun playerAddedToAllowlist(nameAndId: NameAndId) {}

  override fun playerRemovedFromAllowlist(nameAndId: NameAndId) {}

  override fun ipBanned(ipBanListEntry: IpBanListEntry) {}

  override fun ipUnbanned(string: String) {}

  override fun playerBanned(userBanListEntry: UserBanListEntry) {}

  override fun playerUnbanned(nameAndId: NameAndId) {}

  override fun <T : Any> onGameRuleChanged(
    gameRule: GameRule<T>,
    value: T
  ) {
    if(value is Boolean && (gameRule as? GameRule<Boolean>) in GameRuleConfig.BOOL_RULES)
      GameRuleConfig.BOOL_RULES[(gameRule as GameRule<Boolean>)] = value

    if(value is Int && (gameRule as? GameRule<Int>) in GameRuleConfig.INT_RULES)
      GameRuleConfig.INT_RULES[(gameRule as GameRule<Int>)] = value
  }

  override fun statusHeartbeat() {}
}