package com.lovelessgeek.housemanager.shared.converters

interface Converter<T, U> {
    val converter: (T) -> U
    val inverter: (U) -> T
}