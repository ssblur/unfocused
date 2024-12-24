package com.ssblur.unfocused.event

open class SimpleEvent<T>(retroactive: Boolean = false, cancelable: Boolean = false): Event<T, Nothing>(retroactive, cancelable)