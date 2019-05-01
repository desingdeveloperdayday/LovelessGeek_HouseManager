package com.lovelessgeek.housemanager.data

import com.lovelessgeek.housemanager.shared.models.InstantTask
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.shared.models.User
import java.time.LocalDateTime

// TODO: replace with databases
internal val task1 = InstantTask(
    id = "1",
    name = "청소하기",
    time = LocalDateTime.now().plusHours(2L)
)
internal val task2 = InstantTask(
    id = "2",
    name = "세탁하기",
    time = LocalDateTime.now().plusDays(1L)
)
internal val tasks: List<Task> = listOf(
    task1,
    task2
)

internal val user1 = User(
    "1",
    "tura",
    listOf(task1)
)
internal val user2 = User("2", "ddd")
internal val user3 = User(
    "3",
    "alan",
    listOf(task2)
)
internal val users = listOf(
    user1,
    user2,
    user3
)