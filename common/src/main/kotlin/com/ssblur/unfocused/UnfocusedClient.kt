package com.ssblur.unfocused

import com.ssblur.unfocused.event.client.ClientLevelTickEvent
import com.ssblur.unfocused.event.client.ClientScreenRegistrationEvent.registerScreen
import com.ssblur.unfocused.screen.UnfocusedBookScreen
import net.minecraft.client.Minecraft

object UnfocusedClient {
  fun init() {
    Unfocused.BOOK_MENU.then {
      Unfocused.registerScreen(it, ::UnfocusedBookScreen)
    }

    ClientLevelTickEvent.Before.register {
      if(Minecraft.getInstance().player?.hasContainerOpen() != true) {
        UnfocusedBookScreen.backAction = null
      }
    }
  }
}