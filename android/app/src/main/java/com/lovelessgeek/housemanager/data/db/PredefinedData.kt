package com.lovelessgeek.housemanager.data.db

import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.ui.makeTimeFrom
import java.util.Date

fun createCompletedMockData() = listOf(
    Task(
        id = "1",
        category = Category.random(),
        name = "시간 조금 남음",
        isRepeat = true,
        isComplete = true,
        period = 86400000 * 7,
        time = Date(makeTimeFrom(Date(), day = 1, hour = 2)),
        created = Date(makeTimeFrom(Date(), day = -2, hour = -10)),
        completed = Date(makeTimeFrom(Date(), hour = -7))
    ),
    Task(
        id = "2",
        category = Category.random(),
        name = "시간 많이 남음",
        isRepeat = true,
        isComplete = true,
        period = 86400000 * 20,
        time = Date(makeTimeFrom(Date(), day = 8)),
        created = Date(makeTimeFrom(Date(), day = -2, hour = -10)),
        completed = Date(makeTimeFrom(Date(), hour = -7))
    ),
    Task(
        id = "3",
        category = Category.random(),
        name = "오늘",
        isRepeat = true,
        isComplete = true,
        period = 86400000 * 3,
        time = Date(makeTimeFrom(Date(), hour = 2)),
        created = Date(makeTimeFrom(Date(), hour = -10)),
        completed = Date(makeTimeFrom(Date(), hour = -7))
    )
)

fun createMockData() = listOf(
    Task(
        id = "4",
        category = Category.random(),
        name = "시간 조금 남음",
        isRepeat = true,
        period = 86400000 * 7,
        time = Date(makeTimeFrom(Date(), day = 2, hour = 4)),
        created = Date(makeTimeFrom(Date(), day = -2))
    ),
    Task(
        id = "5",
        category = Category.random(),
        name = "시간 많이 남음",
        isRepeat = true,
        period = 86400000 * 20,
        time = Date(makeTimeFrom(Date(), day = 7, hour = 12)),
        created = Date(makeTimeFrom(Date(), day = -2))
    ),
    Task(
        id = "6",
        category = Category.random(),
        name = "오늘",
        isRepeat = true,
        period = 86400000 * 3,
        time = Date(makeTimeFrom(Date(), hour = 20)),
        created = Date(makeTimeFrom(Date(), day = -2))
    ),
    Task(
        id = "7",
        category = Category.random(),
        name = "기한 지남",
        isRepeat = true,
        period = 86400000,
        time = Date(makeTimeFrom(Date(), hour = -2)),
        created = Date(makeTimeFrom(Date(), day = -2))
    )
)
