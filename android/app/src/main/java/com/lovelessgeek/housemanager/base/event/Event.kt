package com.lovelessgeek.housemanager.base.event

/**
 * Event class (with payload)
 * From android developers medium post
 */
open class Event<out T>(private val content: T) {

    var handled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (handled) {
            null
        } else {
            handled = true
            content
        }
    }

    fun peekContent(): T {
        return content
    }
}
