package com.lovelessgeek.housemanager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lovelessgeek.housemanager.shared.models.InstantTask
import com.lovelessgeek.housemanager.shared.models.Task
import com.lovelessgeek.housemanager.shared.models.TaskType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskListAdapter(private val mItems: ArrayList<Task>) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    override fun getItemCount(): Int = mItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        when (item.type) {
            TaskType.INSTANT -> {
                item as InstantTask
                holder.taskName.text = item.name
                holder.startsAt.text = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(Date(item.time))
            }
        }
    }

    /**
     * ViewHolder
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskName: TextView = view.findViewById(R.id.tv_task_name)
        val startsAt: TextView = view.findViewById(R.id.tv_task_starts_at)
    }
}