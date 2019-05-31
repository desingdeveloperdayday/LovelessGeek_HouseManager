package com.lovelessgeek.housemanager.ui.main.notification

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
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BaseFragment
import com.lovelessgeek.housemanager.ext.setItemMargin
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.ui.main.notification.NotificationViewModel.State.Failure
import com.lovelessgeek.housemanager.ui.main.notification.NotificationViewModel.State.Loading
import com.lovelessgeek.housemanager.ui.main.notification.NotificationViewModel.State.Success
import com.lovelessgeek.housemanager.ui.main.notification.adapter.TaskListAdapter
import com.lovelessgeek.housemanager.ui.newtask.NewTaskActivity
import com.lovelessgeek.housemanager.ui.newtask.TaskGuideActivity
import kotlinx.android.synthetic.main.fragment_notification_content.*
import kotlinx.android.synthetic.main.fragment_notification_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

class NotificationFragment : BaseFragment() {

    // FIXME
    object RequestCode {
        const val NEW_TASK = 0x0001
    }

    override val layoutId: Int
        get() = R.layout.fragment_notification_layout

    private val taskAdapter =
        TaskListAdapter()

    private val vm: NotificationViewModel by viewModel()

    var onMenuButtonClicked: (View) -> Unit = {}

    private val textPrimary: Int by lazy { requireContext().getColor(R.color.text_primary) }
    private val textPrimaryDisabled: Int by lazy { requireContext().getColor(R.color.text_primary_16) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clear_db_button.setOnClickListener {
            vm.deleteAll()
        }

        setupTaskList()
        setupButtons()

        bottom_app_bar.setNavigationOnClickListener(onMenuButtonClicked)

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
                ShowingType.TODO -> vm.loadTodos()
                ShowingType.COMPLETED -> vm.loadCompleted()
            }
        }

        vm.moveToNewTask.observe(this) {
            /*
            val intent = Intent(activity, NewTaskActivity::class.java)
            startActivityForResult(
                intent,
                RequestCode.NEW_TASK
            )
            */
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

    private fun setupButtons() {
        task_title_todo.setOnClickListener {
            task_title_todo.setTextColor(textPrimary)
            task_title_completed.setTextColor(textPrimaryDisabled)
            vm.onClickTodo()
        }

        task_title_completed.setOnClickListener {
            task_title_completed.setTextColor(textPrimary)
            task_title_todo.setTextColor(textPrimaryDisabled)
            vm.onClickCompleted()
        }

        // Fab
        fab_add_task.setOnClickListener {
            vm.onClickAdd()
        }
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

    private fun setupTaskList() {
        notification_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
            setItemMargin(16)
        }

        // Task RecyclerView
        taskAdapter.onClickDelete {
            vm.onClickDeleteTask(it)
        }
    }

    private fun setupCategorySpinner(categories: List<Category>) {
        categories
            .map { category -> category.readableName }
            .let {
                if (category_spinner.adapter != null)
                    return

                category_spinner.adapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }

                category_spinner.onItemSelectedListener = object : OnItemSelectedListener {
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