package com.lovelessgeek.housemanager.shared.converters

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.TimeZone

// LocalDateTime <-> Long
object LocalDateTimeConverter :
    Converter<LocalDateTime, Long> {
    override val inverter: (Long) -> LocalDateTime
        get() = { input: Long ->
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(input),
                TimeZone.getDefault().toZoneId()
            )
        }

    override val converter: (LocalDateTime) -> Long
        get() = { input: LocalDateTime ->
            input.toInstant(ZoneOffset.UTC).toEpochMilli()
        }
}
