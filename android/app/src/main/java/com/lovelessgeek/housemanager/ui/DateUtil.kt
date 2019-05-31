package com.lovelessgeek.housemanager.ui

import java.util.Calendar
import java.util.Date

// All inputs: ms

fun Long.toSecond(): Int =
    this.div(1000).toInt()

fun Int.toMillis(): Long =
    this.times(1000).toLong()

fun Long.diff(current: Long = Date().time): Int =
    this.minus(current).toSecond()

fun Long.isOverDue(current: Long = Date().time): Boolean =
    diff(current) < 0

fun Long.lessThanOneDayLeft(current: Long = Date().time): Boolean =
    diff(current) in 1 until 86400

// TODO: 이쁘게 해주는 라이브러리 있음
fun Long.toReadableDateString(): String {
    val hour = 3600 * 1000
    val day = hour * 24
    val week = day * 7

    return if (this >= week)
        "%d주".format(this / week)
    else if (this >= day)
        "%d일".format(this / day)
    else
        "%d시간".format(this / hour)
}

fun makeTime(
    month: Int = 1,
    day: Int = 1,
    hour: Int = 0,
    minute: Int = 0,
    second: Int = 0
): Long {
    return Calendar.getInstance().apply {
        set(2019, month - 1, day, hour, minute, second)
    }.time.time
}
