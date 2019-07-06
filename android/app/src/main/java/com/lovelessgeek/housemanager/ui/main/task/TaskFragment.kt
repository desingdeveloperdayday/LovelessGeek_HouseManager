package com.lovelessgeek.housemanager.ui.main.task

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.appcompat.navigationClicks
import com.jakewharton.rxbinding3.view.clicks
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BindingFragment
import com.lovelessgeek.housemanager.databinding.FragmentTaskLayoutBinding
import com.lovelessgeek.housemanager.ext.getColorCompat
import com.lovelessgeek.housemanager.ext.hide
import com.lovelessgeek.housemanager.ext.preventMultipleEmission
import com.lovelessgeek.housemanager.ext.show
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.ui.main.task.TaskViewModel.State.Failure
import com.lovelessgeek.housemanager.ui.main.task.TaskViewModel.State.Loading
import com.lovelessgeek.housemanager.ui.main.task.TaskViewModel.State.Success
import com.lovelessgeek.housemanager.ui.main.task.adapter.TaskListAdapter
import com.lovelessgeek.housemanager.ui.newtask.NewTaskActivity
import com.lovelessgeek.housemanager.ui.newtask.TaskGuideActivity
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import org.koin.android.viewmodel.ext.android.viewModel

class TaskFragment : BindingFragment<FragmentTaskLayoutBinding>(rx = true) {

    // FIXME
    object RequestCode {
        const val NEW_TASK = 0x0001
    }

    override val layoutId: Int
        get() = R.layout.fragment_task_layout

    var onMenuButtonClicked: () -> Unit = {}

    private val taskAdapter = TaskListAdapter()

    private val vm: TaskViewModel by viewModel()

    private val textPrimary: Int by lazy { requireContext().getColorCompat(R.color.text_primary) }
    private val textPrimaryDisabled: Int by lazy { requireContext().getColorCompat(R.color.text_primary_16) }

    private val colorBlue: Int by lazy { requireContext().getColorCompat(R.color.true_blue) }
    private val colorRed: Int by lazy { requireContext().getColorCompat(R.color.ferrari_red) }

    private val iconAdd: Drawable by lazy { requireContext().getDrawable(R.drawable.ic_add) }
    private val iconCheck: Drawable by lazy { requireContext().getDrawable(R.drawable.ic_check) }

    private val isEditingStream: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.content.clearDbButton.setOnClickListener {
            vm.initializeDatabase()
        }

        setupTaskList()
        setupButtons()

        +binding.bottomAppBar.navigationClicks()
            .subscribe({
                onMenuButtonClicked()
            }, Throwable::printStackTrace)

        vm.state.observe(this) { state ->
            when (state) {
                Loading, Failure -> {
                    TODO()
                }
                is Success -> {
                    state.tasks?.let(taskAdapter::addAll)
                    state.categories?.let(this::setupCategorySpinner)
                    state.newTask?.let(taskAdapter::add)
                }
            }
        }

        vm.showCategory.observe(this) { category ->
            taskAdapter.showOnly(category)
        }

        vm.showingType.observe(this) { showingType ->
            when (showingType) {
                ShowingType.TODO -> {
                    with(binding.content) {
                        taskTitleTodo.setTextColor(textPrimary)
                        taskTitleCompleted.setTextColor(textPrimaryDisabled)
                        sortButton.show()
                        editButton.hide()
                    }

                    vm.loadTodoTasks()
                }
                ShowingType.COMPLETED -> {
                    with(binding.content) {
                        taskTitleTodo.setTextColor(textPrimaryDisabled)
                        taskTitleCompleted.setTextColor(textPrimary)
                        sortButton.hide()
                        editButton.show()
                    }

                    vm.sort(SortMethod.DDAY)
                    vm.loadCompleted()
                }
            }
        }

        vm.sortBy.observe(this) { sortMethod ->
            binding.content.sortButton.text = sortMethod.str
            taskAdapter.sortBy(sortMethod)
        }

        vm.moveToNewTask.observe(this) {
            AlertDialog.Builder(requireActivity())
                .setTitle(R.string.menu_add_task)
                .setItems(R.array.menu_add_task_buttons) { dialog, which ->
                    when (which) {
                        0 -> {  // 할 일 입력
                            val intent = Intent(activity, NewTaskActivity::class.java)
                            startActivityForResult(
                                intent,
                                RequestCode.NEW_TASK
                            )
                        }
                        1 -> {  // 집안일 가이드
                            val intent = Intent(activity, TaskGuideActivity::class.java)
                            startActivityForResult(
                                intent,
                                RequestCode.NEW_TASK
                            )
                        }
                    }
                }.show()
        }

        +isEditingStream
            .subscribe({ isEditing ->
                if (isEditing) enterEditingMode()
                else {
                    leaveEditingMode()
                }
            }, Throwable::printStackTrace)

        +taskAdapter.completedTaskClicks
            .withLatestFrom(isEditingStream)
            .filter { (_, isEditing) -> isEditing }
            .map { (task, _) -> task }
            .subscribe({ task ->
                vm.onClickCompletedTask(task)
            }, Throwable::printStackTrace)
    }

    private fun enterEditingMode() = with(binding) {
        actionFab.setImageDrawable(iconCheck)
        actionFab.backgroundTintList = ColorStateList.valueOf(colorRed)
        content.editButton.text = getString(R.string.task_cancel)
        content.categorySpinner.hide(invisible = true)
        content.selectAllButton.show()
    }

    private fun leaveEditingMode() = with(binding) {
        actionFab.setImageDrawable(iconAdd)
        actionFab.backgroundTintList = ColorStateList.valueOf(colorBlue)
        content.editButton.text = getString(R.string.task_edit)
        content.categorySpinner.show()
        content.selectAllButton.hide()
    }

    private fun setupButtons() = with(binding) {
        +Observable
            .merge(
                content.taskTitleTodo.clicks().map {
                    { vm.onClickTodo() }
                },
                content.taskTitleCompleted.clicks().map {
                    { vm.onClickCompleted() }
                }
            )
            .preventMultipleEmission(300)
            .subscribe({ action ->
                action()
            }, Throwable::printStackTrace)

        val fabClicks = actionFab.clicks()
            .preventMultipleEmission()
            .withLatestFrom(isEditingStream) { _, isEditing -> isEditing }
            .share()

        val editButtonClicks = content.editButton.clicks()
            .preventMultipleEmission()
            .withLatestFrom(isEditingStream) { _, isEditing -> isEditing }
            .share()

        +fabClicks
            .filter { !it }
            .subscribe({
                vm.onClickAdd()
            }, Throwable::printStackTrace)

        +editButtonClicks
            .filter { !it }
            .subscribe({
                isEditingStream.onNext(true)
            }, Throwable::printStackTrace)

        val cancelEdit = editButtonClicks
            .filter { it }
            .doOnNext {
                vm.resetSelections()
            }

        +fabClicks
            .filter { it }
            .doOnNext { vm.deleteTasks() }
            .mergeWith(cancelEdit)
            .subscribe({
                isEditingStream.onNext(false)
            }, Throwable::printStackTrace)

        +content.selectAllButton.clicks()
            .preventMultipleEmission()
            .subscribe({
                vm.onClickSelectAll()
            }, Throwable::printStackTrace)

        +content.sortButton.clicks()
            .preventMultipleEmission()
            .subscribe({
                vm.onClickSort()
            }, Throwable::printStackTrace)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.NEW_TASK -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        val taskName = it.getStringExtra(NewTaskActivity.KEY_TASK_NAME)
                        val date = it.getLongExtra(NewTaskActivity.KEY_DATE, 0)
                        vm.addNewTask(taskName, date)
                    }
                }
            }
        }
    }

    private fun setupTaskList() = with(binding) {
        content.taskList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }
    }

    private fun setupCategorySpinner(categories: List<Category>) = with(binding) {
        categories
            .map { category -> category.readableName }
            .let {
                if (content.categorySpinner.adapter != null)
                    return

                content.categorySpinner.adapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }

                content.categorySpinner.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Does nothing.
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        vm.onCategorySelected(Category.values()[position])
                    }
                }
            }
    }
}