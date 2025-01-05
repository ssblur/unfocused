package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent

object LootTablePopulateEvent: SimpleEvent<String>(retroactive = true, cancelable = false) {

}