package com.lovelessgeek.housemanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lovelessgeek.housemanager.data.LocalDatabase
import com.lovelessgeek.housemanager.data.TaskEntity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Date

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = MainActivity::class.java.simpleName

    private var mFirebaseUser: FirebaseUser? = null

    private val taskItems = ArrayList<TaskEntity>()
    private val taskAdapter = TaskListAdapter(taskItems)

    private var database: LocalDatabase? = null

    // FIXME
    object RequestCode {
        const val NEW_TASK = 0x0001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            return finish()
        }
        mFirebaseUser = firebaseUser

        init()
    }

    private fun init() {
        // Task RecyclerView
        todo_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
            setEmptyView(empty_layout)
        }
        database = LocalDatabase.getInstance(this)
        taskAdapter.database = database
        Thread { database?.getTaskDao()?.loadAllTasks()?.let { taskAdapter.addAll(it) } }.start()

        // Fab
        fab_add_task.setOnClickListener(this)
    }

    /**
     * View.OnClickListener
     */
    override fun onClick(view: View) {
        when (view.id) {
            R.id.fab_add_task -> {
                val intent = Intent(this, NewTaskActivity::class.java)
                startActivityForResult(intent, RequestCode.NEW_TASK)
            }
        }
    }
    // View.OnClickListener

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.NEW_TASK -> {
                if (resultCode == RESULT_OK) {
                    data?.let {
                        val taskName = it.getStringExtra("taskName")
                        val date = it.getLongExtra("date", 0)
                        val task = TaskEntity(
                            id = System.currentTimeMillis().toString(),
                            name = taskName,
                            time = Date(date)
                        )
                        Thread {
                            database?.getTaskDao()?.insert(task)
                        }.start()
                        taskAdapter.add(task)
                    }
                }
            }
        }
    }
}
