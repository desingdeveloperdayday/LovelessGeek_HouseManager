package com.lovelessgeek.housemanager.shared.models

enum class Category(val readableName: String) {
    Default("분류 없음"),
    Trash("쓰레기"),
    Cleaning("부엌청소"),
    Laundry("빨래");

    companion object {
        fun random(): Category {
            return values().random()
        }
    }
}