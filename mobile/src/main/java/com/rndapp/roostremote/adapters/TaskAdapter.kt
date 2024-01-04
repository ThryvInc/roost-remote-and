package com.rndapp.roostremote.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rndapp.roostremote.R
import com.rndapp.roostremote.interfaces.OnItemClickedListener
import com.rndapp.roostremote.models.tasks.Task

class TaskAdapter(private val tasks: List<Task>, private val listener: OnItemClickedListener) :
        androidx.recyclerview.widget.RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_view, parent, false)
        return TaskHolder(view)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.nameTextView.text = tasks[position].name
        holder.itemView.setOnClickListener {
            listener.onViewHolderClicked(holder, position)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    class TaskHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView

        init {
            nameTextView = itemView.findViewById<View>(R.id.tv_name) as TextView
        }

    }
}
