package com.lovelessgeek.housemanager.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.lovelessgeek.housemanager.R.layout
import com.lovelessgeek.housemanager.ui.login.LoginActivity
import com.lovelessgeek.housemanager.ui.main.MainViewModel.MainState.Failure
import com.lovelessgeek.housemanager.ui.main.MainViewModel.MainState.Loading
import com.lovelessgeek.housemanager.ui.main.MainViewModel.MainState.Success
import com.lovelessgeek.housemanager.ui.newtask.NewTaskActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val taskAdapter = TaskListAdapter()

    private val vm: MainViewModel by viewModel()

    // FIXME
    object RequestCode {
        const val NEW_TASK = 0x0001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        setupTaskList()

        // Fab
        fab_add_task.setOnClickListener {
            vm.onClickAdd()
        }

        vm.backToLogin.observe(this) {
            it.runIfNotHandled {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
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
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivityForResult(
                intent,
                RequestCode.NEW_TASK
            )
        }
    }

    private fun setupTaskList() {
        // Task RecyclerView
        taskAdapter.onClickDelete {
            vm.onClickDeleteTask(it)
        }

        todo_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
            setEmptyView(empty_layout)
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
}
