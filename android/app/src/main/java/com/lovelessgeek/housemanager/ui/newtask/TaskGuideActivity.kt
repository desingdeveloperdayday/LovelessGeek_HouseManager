package com.lovelessgeek.housemanager.ui.newtask

import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.BaseActivity
import com.lovelessgeek.housemanager.data.db.TaskEntity
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Task
import kotlinx.android.synthetic.main.activity_task_guide.*

class TaskGuideActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener {

    companion object {
        const val KEY_TASK_NAME = "KEY_TASK_NAME"
        const val KEY_DATE = "KEY_DATE"
    }

    private val taskAdapter = GuidedTaskListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_guide)

        setupChips()
        setupTaskList()
    }

    private fun setupChips() {
        val chips = arrayOf(
            chip_task_cleaning,
            chip_task_dish_washing,
            chip_task_bathroom,
            chip_task_laundry,
            chip_task_waste_sorting,
            chip_task_tidy_up,
            chip_task_etc
        )
        chips.forEach { it.setOnCheckedChangeListener(this) }
    }

    private fun setupTaskList() {
        task_recycler_view.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@TaskGuideActivity)
        }
        taskAdapter.add(
            TaskEntity(
                id = "TASK_ID",
                name = "TASK NAME",
                category = Category.Default,
                isComplete = false,
                isRepeat = false,
                period = Task.PERIOD_NO_REPEAT
            )
        )
        taskAdapter.notifyDataSetChanged()
    }

    /**
     * CompoundButton.OnCheckedChangeListener
     */
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        /**
         * FIXME: AND 조건 처리
         */
        when (buttonView?.id) {
            R.id.chip_task_cleaning -> {
                Toast.makeText(this, "Task cleaning: $isChecked", Toast.LENGTH_SHORT).show()
            }
            R.id.chip_task_dish_washing,
            R.id.chip_task_bathroom,
            R.id.chip_task_laundry,
            R.id.chip_task_waste_sorting,
            R.id.chip_task_tidy_up,
            R.id.chip_task_etc,
            null -> {
                // Does nothing
            }
        }
    }
    // CompoundButton.OnCheckedChangeListener
}
