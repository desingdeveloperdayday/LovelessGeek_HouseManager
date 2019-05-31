package com.lovelessgeek.housemanager.ui.main.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovelessgeek.housemanager.base.event.SimpleEvent
import com.lovelessgeek.housemanager.data.Repository
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.ui.main.notification.NotificationViewModel.State.Success
import com.lovelessgeek.housemanager.ui.makeTime
import kotlinx.coroutines.launch
import java.util.Date

class NotificationViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val _moveToNewTask = MutableLiveData<SimpleEvent>()
    val moveToNewTask: LiveData<SimpleEvent>
        get() = _moveToNewTask

    private val mockData = listOf(
        Task(
            id = "1",
            category = Category.random(),
            name = "기한 지남",
            isRepeat = true,
            period = 86400000,
            time = Date(makeTime(month = 5, day = 31, hour = 16)),
            created = Date(makeTime(month = 5, day = 31, hour = 14))
        ),
        Task(
            id = "2",
            category = Category.random(),
            name = "오늘",
            isRepeat = true,
            period = 86400000 * 3,
            time = Date(makeTime(month = 6, day = 1, hour = 6)),
            created = Date(makeTime(month = 5, day = 31, hour = 14))
        ),
        Task(
            id = "3",
            category = Category.random(),
            name = "시간 조금 남음",
            isRepeat = true,
            period = 86400000 * 7,
            time = Date(makeTime(month = 6, day = 2, hour = 2)),
            created = Date(makeTime(month = 5, day = 28, hour = 14))
        ),
        Task(
            id = "4",
            category = Category.random(),
            name = "시간 많이 남음",
            isRepeat = true,
            period = 86400000 * 20,
            time = Date(makeTime(month = 6, day = 7, hour = 2)),
            created = Date(makeTime(month = 5, day = 28, hour = 14))
        )
    )

    sealed class State {
        object Loading : State()
        data class Success(
            val tasks: List<Task>? = null,
            val categories: List<Category>? = null,
            val newTask: Task? = null
        ) : State()

        object Failure : State()
    }

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        _state.postValue(
            Success(
                tasks = repository.loadAllTasks().takeIf { it.isNotEmpty() } ?: mockData,
                categories = repository.loadCategories()
            )
        )
    }

    fun onClickAdd() {
        _moveToNewTask.value = SimpleEvent()
    }

    // 이렇게 하는게 좋을지 확신이 없다..
    fun addNewTask(taskName: String, date: Long) = viewModelScope.launch {
        val task = makeTaskFromIntent(taskName, date)
        repository.addNewTask(task)
        _state.postValue(Success(newTask = task))
    }

    // TODO: Assign category, isRepeat
    private fun makeTaskFromIntent(taskName: String, date: Long) = Task(
        id = System.currentTimeMillis().toString(),
        name = taskName,
        time = Date(date),
        category = Category.random(),
        isRepeat = true,
        period = (1..14).random() * 86400000L
    )

    fun onClickDeleteTask(taskEntity: Task) = viewModelScope.launch {
        repository.deleteTask(taskEntity)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        _state.postValue(Success(tasks = listOf()))
    }
}