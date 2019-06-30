package com.lovelessgeek.housemanager.ui.main.task.adapter

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.R.drawable
import com.lovelessgeek.housemanager.databinding.ItemTaskCompletedBinding
import com.lovelessgeek.housemanager.ext.goneIf
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Category.Cleaning
import com.lovelessgeek.housemanager.shared.models.Category.Default
import com.lovelessgeek.housemanager.shared.models.Category.Laundry
import com.lovelessgeek.housemanager.shared.models.Category.Trash
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.ui.toReadableDateString
import com.lovelessgeek.housemanager.ui.toSecond
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * ViewHolder for completed tasks
 */
class CompletedTaskViewHolder(private val binding: ItemTaskCompletedBinding) :
    ViewHolder(binding.root) {

    companion object {
        val completedFormat = SimpleDateFormat("yyyy. MM. dd 완료", Locale.KOREA)
    }

    val context: Context
        get() = itemView.context

    fun bind(task: Task, onClick: (Task) -> Unit) {
        with(binding) {
            root.setOnClickListener { onClick(task) }

            if (task.isSelected) {
                detailContainer.background =
                    context.getDrawable(R.drawable.card_background_lightsky_filled)
            } else {
                detailContainer.background =
                    context.getDrawable(R.drawable.card_background_lightsky)
            }

            nameText.text = task.name
            categoryText.text = task.category.readableName
            icon.setImageResource(
                when {
                    task.isSelected -> selectedIcon(task.category)
                    else -> categoryIcon(task.category)
                }
            )

            repeatIcon.goneIf(!task.isRepeat)
            periodText.goneIf(!task.isRepeat)

            if (task.isRepeat) {
                periodText.text = task.period.toSecond().toReadableDateString()
            }

            task.completed?.let { completed ->
                completedText.text = completedFormat.format(completed)
            }
        }
    }

    @DrawableRes
    private fun selectedIcon(category: Category): Int = when (category) {
        Default -> drawable.ic_vacuum_cleaner
        Trash -> drawable.ic_category_trash
        Cleaning -> drawable.ic_category_cleaning_click
        Laundry -> drawable.ic_category_laundry_click
    }

    @DrawableRes
    private fun categoryIcon(category: Category): Int = when (category) {
        Default -> drawable.ic_vacuum_cleaner
        Trash -> drawable.ic_category_trash
        Cleaning -> drawable.ic_category_cleaning
        Laundry -> drawable.ic_category_laundry
    }
}