package com.ssblur.unfocused.event

class Event<T>(val retroactive: Boolean = false) {
    fun interface Listener<T> {
        fun listen(event: T)
    }
    val events: MutableList<T> = mutableListOf()
    val subscribers: MutableList<Listener<T>> = mutableListOf()

    fun callback(event: T) {
        if(retroactive) {
            events += event
        }
        subscribers.forEach{ it.listen(event) }
    }

    fun register(subscriber: Listener<T>) {
        events.forEach{ subscriber.listen(it) }
        subscribers += subscriber
    }
}