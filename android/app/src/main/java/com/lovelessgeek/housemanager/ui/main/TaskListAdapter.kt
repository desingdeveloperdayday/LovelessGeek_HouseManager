package com.lovelessgeek.housemanager.ui.main

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.databinding.ItemTaskBinding
import com.lovelessgeek.housemanager.ext.getDrawableCompat
import com.lovelessgeek.housemanager.ext.goneIf
import com.lovelessgeek.housemanager.ext.hide
import com.lovelessgeek.housemanager.ext.inflateBinding
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
import com.lovelessgeek.housemanager.ui.main.TaskListAdapter.ViewHolder
import com.lovelessgeek.housemanager.ui.toDayNumberOnly
import com.lovelessgeek.housemanager.ui.toReadableDateString
import com.lovelessgeek.housemanager.ui.toSecond
import java.util.Date

class TaskListAdapter() : RecyclerView.Adapter<ViewHolder>() {

    private var onClickDelete: ((Task) -> Unit)? = null

    private val items: MutableList<Task> = mutableListOf()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflateBinding(R.layout.item_task))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun onClickDelete(onClickDelete: ((Task) -> Unit)?) {
        this.onClickDelete = onClickDelete
    }

    fun add(item: Task) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    fun addAll(newItems: List<Task>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    /**
     * ViewHolder
     */
    inner class ViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val context: Context
            get() = itemView.context

        private val cardBackgroundSky =
            context.getDrawableCompat(R.drawable.card_background_sky)

        private val cardBackgroundBlue =
            context.getDrawableCompat(R.drawable.card_background_blue)

        private val cardBackgroundRed =
            context.getDrawableCompat(R.drawable.card_background_red)

        private val normalProgressDrawable =
            context.getDrawableCompat(R.drawable.progress_task_normal)

        private val overDueProgressDrawable =
            context.getDrawableCompat(R.drawable.progress_task_overdue)

        private val todayProgressDrawable =
            context.getDrawableCompat(R.drawable.progress_task_today)

        fun bind(task: Task) {
            with(binding) {
                nameText.text = task.name
                categoryText.text = task.category.readableName
                icon.setImageResource(categoryIcon(task.category))

                repeatIcon.goneIf(!task.isRepeat)
                periodText.goneIf(!task.isRepeat)

                if (task.isRepeat) {
                    periodText.text = task.period.toSecond().toReadableDateString()
                }

                setupProgress(task)
            }
        }

        private fun ItemTaskBinding.setupProgress(task: Task) {
            val created = task.created.time
            val due = task.time.time
            val current = Date().time

            taskProgress.max = due.diff(created)

            when {
                due.isOverDue() -> {
                    detailContainer.background = cardBackgroundRed
                    taskProgress.progressDrawable = overDueProgressDrawable
                    taskProgress.progress = taskProgress.max

                    dayPassed.hide()
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

                    dayPassed.hide()
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

                    dayPassed.show()
                    dayLeft.show()
                    daySpecial.hide()

                    dayPassed.text = timePassed.toReadableDateString()
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
    }
}