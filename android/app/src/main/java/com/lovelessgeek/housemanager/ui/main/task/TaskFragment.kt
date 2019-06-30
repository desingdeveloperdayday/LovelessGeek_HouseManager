package com.lovelessgeek.housemanager.ui.main.task

import android.app.Activity.RESULT_OK
import android.content.Intent
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
import org.koin.android.viewmodel.ext.android.viewModel

class TaskFragment : BindingFragment<FragmentTaskLayoutBinding>(rx = true) {

    // FIXME
    object RequestCode {
        const val NEW_TASK = 0x0001
    }

    override val layoutId: Int
        get() = R.layout.fragment_task_layout

    private val taskAdapter =
        TaskListAdapter()

    private val vm: TaskViewModel by viewModel()

    var onMenuButtonClicked: () -> Unit = {}

    private val textPrimary: Int by lazy { requireContext().getColor(R.color.text_primary) }
    private val textPrimaryDisabled: Int by lazy { requireContext().getColor(R.color.text_primary_16) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FIXME: Remove since it is for debug
        binding.content.clearDbButton.setOnClickListener {
            vm.deleteAll()
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

                    vm.loadTodos()
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

        // Fab
        +fabAddTask.clicks()
            .preventMultipleEmission()
            .subscribe({
                vm.onClickAdd()
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