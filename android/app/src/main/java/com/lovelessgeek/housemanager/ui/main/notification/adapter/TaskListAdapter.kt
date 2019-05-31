package com.lovelessgeek.housemanager.ui.main.notification.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.ext.inflateBinding
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Category.Default
import com.lovelessgeek.housemanager.shared.models.Task

class TaskListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_TODO = 1
        const val VIEW_TYPE_COMPLETED = 2
    }

    private var onClickDelete: ((Task) -> Unit)? = null

    private var category: Category = Default
    private val originalItems: MutableList<Task> = mutableListOf()
    private val items: MutableList<Task> = mutableListOf()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isComplete) VIEW_TYPE_COMPLETED else VIEW_TYPE_TODO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TODO -> TodoTaskViewHolder(
                parent.inflateBinding(
                    R.layout.item_task_todo
                )
            )
            VIEW_TYPE_COMPLETED -> CompletedTaskViewHolder(
                parent.inflateBinding(R.layout.item_task_completed)
            )
            else -> throw IllegalArgumentException("Error while creating view holder: Invalid view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        val viewType = getItemViewType(position)

        when (viewType) {
            VIEW_TYPE_TODO -> (holder as TodoTaskViewHolder).bind(item)
            VIEW_TYPE_COMPLETED -> (holder as CompletedTaskViewHolder).bind(item)
            else -> throw IllegalArgumentException("Error while binding view holder: Invalid view type $viewType")
        }
    }

    fun onClickDelete(onClickDelete: ((Task) -> Unit)?) {
        this.onClickDelete = onClickDelete
    }

    fun add(item: Task) {
        originalItems.add(item)
        applyFilterOnItem(item)
    }

    fun addAll(newItems: List<Task>) {
        originalItems.clear()
        originalItems.addAll(newItems)
        applyFilter()
    }

    fun showOnly(category: Category) {
        if (this.category != category) {
            this.category = category
            applyFilter()
        }
    }

    private fun applyFilter() {
        items.clear()
        items.addAll(originalItems.let { items ->
            if (category == Default)
                items
            else
                items.filter { task -> task.category == category }
        })
        notifyDataSetChanged()
    }

    private fun applyFilterOnItem(item: Task) {
        if (category == Default || item.category == category) {
            items.add(item)
            notifyItemInserted(items.lastIndex)
        }
    }

}