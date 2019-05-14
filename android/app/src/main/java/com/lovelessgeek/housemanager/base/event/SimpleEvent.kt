package com.lovelessgeek.housemanager.base.event

/**
 * Event class for simple event (no payload)
 * From android developers medium post
 */
class SimpleEvent {
    var handled = false
        private set

    fun runIfNotHandled(block: () -> Unit) {
        if (handled) return

        handled = true
        block()
    }
}
