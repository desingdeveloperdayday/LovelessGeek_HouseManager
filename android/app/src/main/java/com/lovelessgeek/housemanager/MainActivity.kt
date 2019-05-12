package com.lovelessgeek.housemanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lovelessgeek.housemanager.data.Provider
import com.lovelessgeek.housemanager.data.Repository
import com.lovelessgeek.housemanager.data.db.TaskEntity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private var mFirebaseUser: FirebaseUser? = null

    private val taskAdapter = TaskListAdapter()

    private val repository: Repository by lazy { Provider.provideRepository(this) }

    // FIXME
    object RequestCode {
        const val NEW_TASK = 0x0001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseAuth.getInstance().currentUser
            ?.let { mFirebaseUser = it }
            ?: run(this::backToLogin)

        init()
    }

    private fun backToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun init() {
        // Task RecyclerView
        taskAdapter.onClickDelete {
            Thread { repository.deleteTask(it) }
        }

        todo_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
            setEmptyView(empty_layout)
        }

        Thread {
            repository.loadAllTasks().let { taskAdapter.addAll(it) }
        }.start()

        // Fab
        fab_add_task.setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivityForResult(intent, RequestCode.NEW_TASK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.NEW_TASK -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        makeTaskFromIntent(it).let { task ->
                            Thread { repository.addNewTask(task) }.start()
                            taskAdapter.add(task)
                        }
                    }
                }
            }
        }
    }

    private fun makeTaskFromIntent(intent: Intent): TaskEntity {
        val taskName = intent.getStringExtra("taskName")
        val date = intent.getLongExtra("date", 0)
        return TaskEntity(
            id = System.currentTimeMillis().toString(),
            name = taskName,
            time = Date(date)
        )
    }
}
