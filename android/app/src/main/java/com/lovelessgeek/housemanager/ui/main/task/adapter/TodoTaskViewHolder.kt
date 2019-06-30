package com.lovelessgeek.housemanager.ui.main.task.adapter

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.databinding.ItemTaskTodoBinding
import com.lovelessgeek.housemanager.ext.goneIf
import com.lovelessgeek.housemanager.ext.hide
import com.lovelessgeek.housemanager.ext.show
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Category.Cleaning
import com.lovelessgeek.housemanager.shared.models.Category.Default
import com.lovelessgeek.housemanager.shared.models.Category.Laundry
import com.lovelessgeek.housemanager.shared.models.Category.Trash
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.ui.diff
import com.lovelessgeek.housemanager.ui.isOverDue
import com.lovelessgeek.housemanager.ui.lessThanOneDayLeft
import com.lovelessgeek.housemanager.ui.toDayNumberOnly
import com.lovelessgeek.housemanager.ui.toReadableDateString
import com.lovelessgeek.housemanager.ui.toSecond
import java.util.Date

/**
 * ViewHolder for normal tasks
 */
class TodoTaskViewHolder(private val binding: ItemTaskTodoBinding) :
    ViewHolder(binding.root) {

    val context: Context
        get() = itemView.context

    private val cardBackgroundSky =
        context.getDrawable(R.drawable.card_background_sky)

    private val cardBackgroundBlue =
        context.getDrawable(R.drawable.card_background_blue)

    private val cardBackgroundRed =
        context.getDrawable(R.drawable.card_background_red)

    private val normalProgressDrawable =
        context.getDrawable(R.drawable.progress_task_normal)

    private val overDueProgressDrawable =
        context.getDrawable(R.drawable.progress_task_overdue)

    private val todayProgressDrawable =
        context.getDrawable(R.drawable.progress_task_today)

    fun bind(task: Task) {
        with(binding) {
            nameText.text = task.name
            categoryText.text = task.category.readableName
            icon.setImageResource(
                when {
                    task.time.time.isOverDue() -> incompleteIcon(task.category)
                    else -> categoryIcon(task.category)
                }
            )

            repeatIcon.goneIf(!task.isRepeat)
            periodText.goneIf(!task.isRepeat)

            if (task.isRepeat) {
                periodText.text = task.period.toSecond().toReadableDateString()
            }

            setupProgress(task)
        }
    }

    private fun ItemTaskTodoBinding.setupProgress(task: Task) {
        val created = task.created.time
        val due = task.time.time
        val current = Date().time

        taskProgress.max = due.diff(created)

        when {
            due.isOverDue() -> {
                detailContainer.background = cardBackgroundRed
                taskProgress.progressDrawable = overDueProgressDrawable
                taskProgress.progress = taskProgress.max

                dayLeft.hide()
                daySpecial.show()
                daySpecial.text = context.getString(
                    R.string.task_day_overdue,
                    current.diff(due).toReadableDateString()
                )
            }
            due.lessThanOneDayLeft() -> {
                detailContainer.background = cardBackgroundBlue
                taskProgress.progressDrawable = todayProgressDrawable
                taskProgress.progress = taskProgress.max

                dayLeft.hide()
                daySpecial.show()
                daySpecial.text = context.getString(R.string.task_day_today)
            }
            else -> {
                val timePassed = current.diff(created)
                val timeLeft = due.diff(current)

                detailContainer.background = cardBackgroundSky
                taskProgress.progressDrawable = normalProgressDrawable
                taskProgress.progress = timePassed

                dayLeft.show()
                daySpecial.hide()

                dayLeft.text =
                    context.getString(R.string.task_day_left, timeLeft.toDayNumberOnly())
            }
        }
    }

    @DrawableRes
    private fun categoryIcon(category: Category): Int = when (category) {
        Default -> R.drawable.ic_vacuum_cleaner
        Trash -> R.drawable.ic_category_trash
        Cleaning -> R.drawable.ic_category_cleaning
        Laundry -> R.drawable.ic_category_laundry
    }

    @DrawableRes
    private fun incompleteIcon(category: Category): Int = when (category) {
        Default -> R.drawable.ic_vacuum_cleaner
        Trash -> R.drawable.ic_category_trash_incomplete
        Cleaning -> R.drawable.ic_category_cleaning_incomplete
        Laundry -> R.drawable.ic_category_laundry_incomplete
    }
}