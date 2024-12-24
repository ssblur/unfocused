package com.ssblur.unfocused.event


open class Event<T>(val retroactive: Boolean = false, val cancellable: Boolean = false) {
    fun interface Listener<T> {
        fun listen(event: T)
    }
    val events: MutableList<T> = mutableListOf()
    val subscribers: MutableList<Listener<T>> = mutableListOf()
    var cancelled = false

    fun callback(event: T) {
        if(retroactive) events += event
        cancelled = false
        subscribers.forEach{ it.listen(event) }
    }

    fun register(subscriber: Listener<T>) {
        events.forEach{ subscriber.listen(it) }
        subscribers += subscriber
    }

    fun cancel() {
        if(!cancellable) throw Exception("Attempted to cancel uncancellable event")
        cancelled = true
    }
    fun isCancelled() = cancelled
}