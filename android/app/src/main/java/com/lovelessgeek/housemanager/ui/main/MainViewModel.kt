package com.lovelessgeek.housemanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lovelessgeek.housemanager.base.event.SimpleEvent
import com.lovelessgeek.housemanager.data.Repository
import com.lovelessgeek.housemanager.data.db.TaskEntity
import com.lovelessgeek.housemanager.runAndForget
import com.lovelessgeek.housemanager.ui.main.MainViewModel.MainState.Success
import java.util.Date

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _backToLogin = MutableLiveData<SimpleEvent>()
    val backToLogin: LiveData<SimpleEvent>
        get() = _backToLogin

    private val _moveToNewTask = MutableLiveData<SimpleEvent>()
    val moveToNewTask: LiveData<SimpleEvent>
        get() = _moveToNewTask

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState>
        get() = _state

    private var mFirebaseUser: FirebaseUser?

    sealed class MainState {
        object Loading : MainState()
        object Failure : MainState()
        class Success(
            val tasks: List<TaskEntity>? = null,
            val newTask: TaskEntity? = null
        ) : MainState()
    }

    init {
        mFirebaseUser = FirebaseAuth.getInstance().currentUser
            ?: run {
                _backToLogin.value = SimpleEvent()
                null
            }

        runAndForget {
            loadAllTasks()
        }
    }

    private fun loadAllTasks() {
        repository.loadAllTasks().let { tasks ->
            // Should use postValue() here, because it is not in main thread.
            _state.postValue(Success(tasks = tasks))
        }
    }

    fun onClickDeleteTask(taskEntity: TaskEntity) {
        runAndForget {
            repository.deleteTask(taskEntity)
        }
    }

    fun onClickAdd() {
        _moveToNewTask.value = SimpleEvent()
    }

    // 이렇게 하는게 좋을지 확신이 없다..
    fun addNewTask(taskName: String, date: Long) {
        runAndForget {
            val task = makeTaskFromIntent(taskName, date)
            repository.addNewTask(task)
            _state.postValue(Success(newTask = task))
        }
    }

    private fun makeTaskFromIntent(taskName: String, date: Long) = TaskEntity(
        id = System.currentTimeMillis().toString(),
        name = taskName,
        time = Date(date)
    )
}