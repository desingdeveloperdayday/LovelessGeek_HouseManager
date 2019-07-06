package com.lovelessgeek.housemanager.shared.models

data class User(
    val id: String,
    val username: String,
    val tasks: List<Task> = listOf()
)
