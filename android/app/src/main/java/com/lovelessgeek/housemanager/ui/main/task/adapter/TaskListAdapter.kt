package com.lovelessgeek.housemanager.ui.main.task.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.base.SimpleItemCallback
import com.lovelessgeek.housemanager.ext.inflateBinding
import com.lovelessgeek.housemanager.shared.models.Category
import com.lovelessgeek.housemanager.shared.models.Category.Default
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.ui.main.task.SortMethod
import com.lovelessgeek.housemanager.ui.main.task.SortMethod.DDAY
import com.lovelessgeek.housemanager.ui.main.task.SortMethod.NAME
import io.reactivex.subjects.PublishSubject

class TaskListAdapter : ListAdapter<Task, ViewHolder>(diff) {

    companion object {
        const val VIEW_TYPE_TODO = 1
        const val VIEW_TYPE_COMPLETED = 2

        val diff = object : SimpleItemCallback<Task>({ id }) {
            override fun getChangePayload(oldItem: Task, newItem: Task) =
                !oldItem.isSelected && newItem.isSelected
        }
    }

    private var category: Category = Default
    private var sortMethod: SortMethod = DDAY

    private val originalItems: MutableList<Task> = mutableListOf()
    private val items: MutableList<Task> = mutableListOf()

    val completedTaskClicks: PublishSubject<Task> = PublishSubject.create()

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isComplete) VIEW_TYPE_COMPLETED else VIEW_TYPE_TODO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val viewType = getItemViewType(position)

        when (viewType) {
            VIEW_TYPE_TODO -> (holder as TodoTaskViewHolder).bind(item)
            VIEW_TYPE_COMPLETED -> (holder as CompletedTaskViewHolder).bind(
                item,
                completedTaskClicks::onNext
            )
            else -> throw IllegalArgumentException("Error while binding view holder: Invalid view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        val isSelected = payloads.getOrNull(0) as? Boolean ?: false

        if (isSelected) {
            val item = getItem(position)
            val viewType = getItemViewType(position)

            if (viewType == VIEW_TYPE_COMPLETED) {
                (holder as CompletedTaskViewHolder).bind(
                    item,
                    completedTaskClicks::onNext
                )
            } else {
                throw IllegalArgumentException(
                    "Error while binding view holder with payloads: Invalid view type $viewType"
                )
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    // Indicates that the code mutates the state of RecyclerView.
    private fun mutation(action: () -> Unit) {
        action()
        sortItems()
        submitList(items.toList())
    }

    fun add(item: Task) = mutation {
        originalItems.add(item)
        applyFilterOnItem(item)
    }

    fun addAll(newItems: List<Task>) = mutation {
        originalItems.clear()
        originalItems.addAll(newItems.map { it.copy() })
        applyFilter()
    }

    fun showOnly(category: Category) = mutation {
        if (this.category != category) {
            this.category = category
            applyFilter()
        }
    }

    fun sortBy(sortMethod: SortMethod) {
        if (this.sortMethod != sortMethod) {
            mutation { this.sortMethod = sortMethod }
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
    }

    private fun sortItems() {
        when (sortMethod) {
            DDAY -> items.sortBy { item -> item.time.time }
            NAME -> items.sortBy { item -> item.name }
        }
    }

    private fun applyFilterOnItem(item: Task) {
        if (category == Default || item.category == category) {
            items.add(item)
        }
    }
}