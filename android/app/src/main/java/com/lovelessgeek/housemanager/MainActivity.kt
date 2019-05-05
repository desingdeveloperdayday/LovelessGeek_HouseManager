package com.lovelessgeek.housemanager

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.lovelessgeek.housemanager.shared.models.InstantTask
import com.lovelessgeek.housemanager.shared.models.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = MainActivity::class.java.simpleName

    private val taskItems = ArrayList<Task>()
    private val taskAdapter = TaskListAdapter(taskItems)

    // FIXME
    object RequestCode {
        const val NEW_TASK = 0x0001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        // Task RecyclerView
        todo_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
            setEmptyView(empty_layout)
        }
        taskAdapter.notifyDataSetChanged()

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
                        val taskName = it.getStringExtra("title")
                        Log.d(TAG, "title: $title")
                        val period = it.getLongExtra("startsAt", 0)
                        taskItems.add(InstantTask(id=System.currentTimeMillis().toString(), name=taskName, time=period))
                        taskAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}
