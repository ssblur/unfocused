package com.ssblur.unfocused.event


open class Event<T, R>(
  val retroactive: Boolean = false,
  val cancelable: Boolean = false,
  val clearAfterRun: Boolean = true
) {
  fun interface Listener<T> {
    fun listen(event: T)
  }

  val events: MutableList<T> = mutableListOf()
  val subscribers: MutableList<Listener<T>> = mutableListOf()
  var cancelled = false
  var value: R? = null

  open fun callback(event: T) {
    if (retroactive)
      if (!clearAfterRun || subscribers.isEmpty())
        events += event
    cancelled = false
    value = null
    subscribers.forEach { it.listen(event) }
  }

  fun register(subscriber: Listener<T>) {
    events.forEach { subscriber.listen(it) }
    if (clearAfterRun) events.clear()
    subscribers += subscriber
  }

  fun cancel() {
    if (!cancelable) throw Exception("Attempted to cancel uncancelable event")
    cancelled = true
  }

  fun cancel(value: R) {
    if (!cancelable) throw Exception("Attempted to cancel uncancelable event")
    cancelled = true
    this.value = value
  }

  fun isCancelled() = cancelled
}