package com.ssblur.unfocused.event

open class LoadEvent<T>(cancelable: Boolean = false):
  Event<T, Nothing>(retroactive = true, cancelable, clearAfterRun = false) {
  override fun callback(event: T) {
    super.callback(event)
    subscribers.clear()
  }
}