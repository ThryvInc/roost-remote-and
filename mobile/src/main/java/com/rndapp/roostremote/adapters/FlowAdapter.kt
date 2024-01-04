package com.rndapp.roostremote.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rndapp.roostremote.models.Flow
import com.rndapp.roostremote.R
import com.rndapp.roostremote.interfaces.OnItemClickedListener
import com.rndapp.roostremote.interfaces.OnItemLongClickedListener

class FlowAdapter(private val flows: List<Flow>, private val listener: OnItemClickedListener,
                  private val longListener: OnItemLongClickedListener? = null) : androidx.recyclerview.widget.RecyclerView.Adapter<FlowAdapter.FlowHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlowHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_view, parent, false)
        return FlowHolder(view)
    }

    override fun onBindViewHolder(holder: FlowHolder, position: Int) {
        holder.nameTextView.text = flows[position].name
        holder.itemView.setOnClickListener {
            listener.onViewHolderClicked(holder, holder.adapterPosition)
        }
        holder.itemView.setOnLongClickListener(object: View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                longListener?.onViewHolderLongClicked(holder, holder.adapterPosition)
                return longListener != null
            }
        })
    }

    override fun getItemCount(): Int {
        return flows.size
    }

    class FlowHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView

        init {
            nameTextView = itemView.findViewById<View>(R.id.tv_name) as TextView
        }

    }
}