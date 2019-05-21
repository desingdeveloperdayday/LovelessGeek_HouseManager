package com.lovelessgeek.housemanager.ui.newtask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lovelessgeek.housemanager.R
import com.lovelessgeek.housemanager.data.db.TaskEntity
import com.lovelessgeek.housemanager.shared.models.TaskType
import com.lovelessgeek.housemanager.ui.newtask.GuidedTaskListAdapter.ViewHolder

class GuidedTaskListAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var onClickDelete: ((TaskEntity) -> Unit)? = null

    private val mItems: MutableList<TaskEntity> = mutableListOf()

    override fun getItemCount(): Int = mItems.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_guide, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        when (item.type) {
            TaskType.INSTANT -> {
                // TODO: item as InstantTask
                holder.taskCategory.text = item.type
                holder.taskName.text = item.name
                holder.taskRating.rating = 2.5f
            }
        }
    }

    fun onClickDelete(onClickDelete: ((TaskEntity) -> Unit)?) {
        this.onClickDelete = onClickDelete
    }

    fun add(item: TaskEntity) {
        mItems.add(item)
        notifyDataSetChanged()
    }

    fun addAll(items: List<TaskEntity>) {
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    /**
     * ViewHolder
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskCategory: TextView = view.findViewById(R.id.tv_category)
        val taskName: TextView = view.findViewById(R.id.tv_task_name)
        val taskRating: RatingBar = view.findViewById(R.id.task_rating)
    }
}