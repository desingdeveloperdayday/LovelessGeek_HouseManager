package com.lovelessgeek.housemanager.ui.main

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BaseFragment
import com.lovelessgeek.housemanager.setSupportActionBar
import com.lovelessgeek.housemanager.ui.main.NotificationViewModel.State.Failure
import com.lovelessgeek.housemanager.ui.main.NotificationViewModel.State.Loading
import com.lovelessgeek.housemanager.ui.main.NotificationViewModel.State.Success
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSupportActionBar(bottom_app_bar)

        setupTaskList()

        // Fab
        fab_add_task.setOnClickListener {
            vm.onClickAdd()
        }

        vm.state.observe(this) { state ->
            when (state) {
                Loading, Failure -> {
                    TODO()
                }
                is Success -> {
                    state.tasks?.let(taskAdapter::addAll)
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
        todo_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
            setEmptyView(empty_layout)
        }

        // Task RecyclerView
        taskAdapter.onClickDelete {
            vm.onClickDeleteTask(it)
        }
    }
}