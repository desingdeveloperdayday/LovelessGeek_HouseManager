package com.lovelessgeek.housemanager

import android.os.AsyncTask
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.lovelessgeek.housemanager.data.LocalDatabase
import com.lovelessgeek.housemanager.data.TaskEntity
import com.lovelessgeek.housemanager.shared.models.TaskType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskListAdapter(private val mItems: ArrayList<TaskEntity>) : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    var database: LocalDatabase? = null

    override fun getItemCount(): Int = mItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        when (item.type) {
            TaskType.INSTANT -> {
                //item as InstantTask
                holder.taskName.text = item.name
                holder.startsAt.text = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).format(item.time)
            }
        }
        holder.deleteButton.setOnClickListener {
            val task = mItems[position]
            AsyncTask.execute { database?.getTaskDao()?.deleteTask(task) }
            mItems.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
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
        val taskName: TextView = view.findViewById(R.id.tv_task_name)
        val startsAt: TextView = view.findViewById(R.id.tv_task_starts_at)
        val deleteButton: ImageButton = view.findViewById(R.id.ib_delete)
    }
}