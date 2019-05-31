package com.lovelessgeek.housemanager.ui.newtask

import android.content.Intent
import android.os.Bundle
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BaseActivity
import kotlinx.android.synthetic.main.activity_new_task.*

class NewTaskActivity : BaseActivity() {

    companion object {
        const val KEY_TASK_NAME = "key_task_name"
        const val KEY_DATE = "key_date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        btn_add_task.setOnClickListener {
            /*
            val calendar = Calendar.getInstance()
            calendar.set(
                dp_task_starts_at.year,
                dp_task_starts_at.month,
                dp_task_starts_at.dayOfMonth
            )
            */

            val intent = Intent()
            val taskName = et_taskname.text.toString().takeIf { it.isNotEmpty() }
                ?: getString(R.string.no_name)
            // TODO: 카테고리 (com.lovelessgeek.housemanager.shared.models.Category) 목록 보여주기
            //val loop = cb_loop.isChecked
            //val notification = cb_notification.isChecked
            intent.putExtra(KEY_TASK_NAME, taskName)
            //intent.putExtra(KEY_DATE, calendar.timeInMillis)
            // FIXME: 등록 시간 기준으로 주기로 알림을 줘야 할 것 같다.
            intent.putExtra(KEY_DATE, System.currentTimeMillis())
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
