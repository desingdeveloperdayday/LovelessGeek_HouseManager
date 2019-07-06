package com.lovelessgeek.housemanager.ui.main.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovelessgeek.housemanager.base.event.SimpleEvent
import com.lovelessgeek.housemanager.data.Repository
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.ui.main.task.SortMethod.DDAY
import com.lovelessgeek.housemanager.ui.main.task.SortMethod.NAME
import com.lovelessgeek.housemanager.ui.main.task.TaskViewModel.State.Success
import kotlinx.coroutines.launch
import java.util.Date

class TaskViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val _showCategory = MutableLiveData<Category>()
    val showCategory: LiveData<Category>
        get() = _showCategory

    private val _showingType = MutableLiveData<ShowingType>(ShowingType.TODO)
    val showingType: LiveData<ShowingType>
        get() = _showingType

    private val _sortBy = MutableLiveData<SortMethod>(DDAY)
    val sortBy: LiveData<SortMethod>
        get() = _sortBy

    private val _moveToNewTask = MutableLiveData<SimpleEvent>()
    val moveToNewTask: LiveData<SimpleEvent>
        get() = _moveToNewTask

    private val completedTasks: MutableList<Task> = mutableListOf()

    sealed class State {
        object Loading : State()
        data class Success(
            val tasks: List<Task>? = null,
            val categories: List<Category>? = null,
            val newTask: Task? = null
        ) : State()

        object Failure : State()
    }

    fun loadTodoTasks() = viewModelScope.launch {
        _state.postValue(
            Success(
                tasks = repository.loadTodoTasks(),
                categories = repository.loadCategories()
            )
        )
    }

    fun loadCompleted() = viewModelScope.launch {
        loadCompletedTasks()
    }

    private suspend fun loadCompletedTasks() {
        completedTasks.clear()
        completedTasks.addAll(repository.loadCompletedTasks())

        _state.postValue(
            Success(
                tasks = completedTasks.deepCopy(),
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

    fun deleteTasks() = viewModelScope.launch {
        val targets = completedTasks.filter { it.isSelected }

        repository.deleteTasks(targets)
        loadCompletedTasks()
    }

    fun resetSelections() {
        completedTasks.forEach { task ->
            task.isSelected = false
        }

        _state.postValue(Success(tasks = completedTasks.deepCopy()))
    }

    fun onClickSelectAll() {
        completedTasks.forEach { task ->
            task.isSelected = true
        }

        _state.postValue(Success(tasks = completedTasks.deepCopy()))
    }

    fun initializeDatabase() = viewModelScope.launch {
        repository.initialize()

        showingType.value?.let { showingType ->
            when (showingType) {
                ShowingType.TODO -> loadTodoTasks()
                ShowingType.COMPLETED -> loadCompletedTasks()
            }
        }
    }

    fun onCategorySelected(category: Category) {
        _showCategory.postValue(category)
    }

    fun onClickTodo() {
        _showingType.postValue(ShowingType.TODO)
    }

    fun onClickCompleted() {
        _showingType.postValue(ShowingType.COMPLETED)
    }

    fun onClickCompletedTask(task: Task) {
        completedTasks
            .find { target -> target.id == task.id }
            ?.run {
                isSelected = !isSelected
            }

        _state.postValue(Success(tasks = completedTasks.deepCopy()))
    }

    fun onClickSort() {
        val sortMethod = when (_sortBy.value) {
            DDAY -> NAME
            NAME -> DDAY
            else -> throw IllegalStateException("Could not be null.")
        }

        _sortBy.postValue(sortMethod)
    }

    fun sort(sortMethod: SortMethod) {
        _sortBy.postValue(sortMethod)
    }

    private fun List<Task>.deepCopy(): List<Task> = map { it.copy() }
}