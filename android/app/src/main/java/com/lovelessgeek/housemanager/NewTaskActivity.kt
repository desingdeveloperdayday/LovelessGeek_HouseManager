package com.lovelessgeek.housemanager

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new_task.*
import java.util.Calendar

class NewTaskActivity : BaseActivity() {

    companion object {
        const val KEY_TASK_NAME = "key_task_name"
        const val KEY_DATE = "key_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        btn_create_task.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(
                dp_task_starts_at.year,
                dp_task_starts_at.month,
                dp_task_starts_at.dayOfMonth
            )

            val intent = Intent()
            intent.putExtra(KEY_TASK_NAME, et_task_name.text.toString())
            intent.putExtra(KEY_DATE, calendar.timeInMillis)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
