package com.lovelessgeek.housemanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovelessgeek.housemanager.base.event.SimpleEvent
import com.lovelessgeek.housemanager.data.Repository
import com.lovelessgeek.housemanager.data.db.TaskEntity
import com.lovelessgeek.housemanager.ui.main.NotificationViewModel.State.Success
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

    sealed class State {
        object Loading : State()
        data class Success(
            val tasks: List<TaskEntity>? = null,
            val newTask: TaskEntity? = null
        ) : State()

        object Failure : State()
    }

    init {
        loadAllTasks()
    }

    private fun loadAllTasks() = viewModelScope.launch {
        repository.loadAllTasks().let { tasks ->
            // Should use postValue() here, because it is not in main thread.
            _state.postValue(Success(tasks = tasks))
        }
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

    private fun makeTaskFromIntent(taskName: String, date: Long) = TaskEntity(
        id = System.currentTimeMillis().toString(),
        name = taskName,
        time = Date(date)
    )

    fun onClickDeleteTask(taskEntity: TaskEntity) = viewModelScope.launch {
        repository.deleteTask(taskEntity)
    }

}