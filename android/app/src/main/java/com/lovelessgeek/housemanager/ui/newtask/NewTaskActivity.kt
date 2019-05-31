package com.lovelessgeek.housemanager.ui.newtask

import android.os.Bundle
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BaseActivity
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

        btn_add_task.setOnClickListener {
            val calendar = Calendar.getInstance()
            /*
            calendar.set(
                dp_task_starts_at.year,
                dp_task_starts_at.month,
                dp_task_starts_at.dayOfMonth
            )

            val intent = Intent()
            val taskName = et_task_name.text.toString().takeIf { it.isNotEmpty() }
                ?: getString(R.string.no_name)
            intent.putExtra(KEY_TASK_NAME, taskName)
            intent.putExtra(KEY_DATE, calendar.timeInMillis)
            setResult(RESULT_OK, intent)
            */
            finish()
        }
    }
}
