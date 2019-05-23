package com.lovelessgeek.housemanager.shared.models

import java.util.Date

/**
 * period: Period in ms. 0 if not repeatable.
 * time: Next due date (specific time to do this task.)
 */
data class Task(
    val id: String,
    val category: Category,
    val name: String,
    val time: Date,
    val isComplete: Boolean = false,
    val isRepeat: Boolean = false,
    val period: Int = PERIOD_NO_REPEAT
) {
    companion object {
        const val PERIOD_NO_REPEAT = 0
    }
}
