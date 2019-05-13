package com.lovelessgeek.housemanager.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lovelessgeek.housemanager.R.id
import com.lovelessgeek.housemanager.R.layout
import com.lovelessgeek.housemanager.data.db.TaskEntity
import com.lovelessgeek.housemanager.shared.models.TaskType
import com.lovelessgeek.housemanager.ui.main.TaskListAdapter.ViewHolder
import java.text.SimpleDateFormat
import java.util.Locale

class TaskListAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var onClickDelete: ((TaskEntity) -> Unit)? = null

    private val mItems: MutableList<TaskEntity> = mutableListOf()

    override fun getItemCount(): Int = mItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        when (item.type) {
            TaskType.INSTANT -> {
                //item as InstantTask
                holder.taskName.text = item.name
                holder.startsAt.text =
                    SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(item.time)
            }
        }
        holder.deleteButton.setOnClickListener {
            val task = mItems[position]
            mItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)

            onClickDelete?.invoke(task)
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
        val taskName: TextView = view.findViewById(id.tv_task_name)
        val startsAt: TextView = view.findViewById(id.tv_task_starts_at)
        val deleteButton: ImageButton = view.findViewById(id.ib_delete)
    }
}