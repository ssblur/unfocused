package com.ssblur.unfocused

import com.ssblur.unfocused.event.client.ClientScreenRegistrationEvent.registerScreen
import com.ssblur.unfocused.screen.UnfocusedBookScreen

object UnfocusedClient {
  fun init() {
    Unfocused.BOOK_MENU.then {
      Unfocused.registerScreen(it, ::UnfocusedBookScreen)
    }
  }
}