package com.lovelessgeek.housemanager

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_new_task.*
import java.util.*

class NewTaskActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = NewTaskActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        btn_create_task.setOnClickListener(this)
    }

    /**
     * View.OnClickListener
     */
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_create_task -> {
                val calendar = Calendar.getInstance()
                calendar.set(dp_task_starts_at.year, dp_task_starts_at.month, dp_task_starts_at.dayOfMonth)

                val intent = Intent()
                intent.putExtra("title", et_task_name.text.toString())
                Log.d(TAG, "title: $title")
                intent.putExtra("startsAt", calendar.timeInMillis)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
    // View.OnClickListener
}
