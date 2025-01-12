package com.ssblur.unfocused.event

open class SimpleEvent<T>(retroactive: Boolean = false, cancelable: Boolean = false, clearAfterRun: Boolean = true):
    Event<T, Nothing>(retroactive, cancelable, clearAfterRun)