package com.lovelessgeek.housemanager.ui.main.notification

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BaseFragment
import com.lovelessgeek.housemanager.ext.setItemMargin
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.ui.main.TaskListAdapter
import com.lovelessgeek.housemanager.ui.main.notification.NotificationViewModel.State.Failure
import com.lovelessgeek.housemanager.ui.main.notification.NotificationViewModel.State.Loading
import com.lovelessgeek.housemanager.ui.main.notification.NotificationViewModel.State.Success
import com.lovelessgeek.housemanager.ui.newtask.NewTaskActivity
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

    private val taskAdapter = TaskListAdapter()

    private val vm: NotificationViewModel by viewModel()

    var onMenuButtonClicked: (View) -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTaskList()

        // Fab
        fab_add_task.setOnClickListener {
            vm.onClickAdd()
        }

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

        vm.moveToNewTask.observe(this) {
            val intent = Intent(activity, NewTaskActivity::class.java)
            startActivityForResult(
                intent,
                RequestCode.NEW_TASK
            )
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
                category_spinner.adapter =
                    ArrayAdapter(context, android.R.layout.simple_spinner_item, it).apply {
                        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }
            }
    }
}