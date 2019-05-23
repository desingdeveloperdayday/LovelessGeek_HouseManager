package com.lovelessgeek.housemanager.shared.models

data class Category(
    val name: String
) {
    companion object {
        val default = Category("default")
    }
}