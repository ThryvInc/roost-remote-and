package com.rndapp.roostremote.models

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.rndapp.roostremote.R
import com.rndapp.roostremote.interfaces.OnDeviceClickedListener

/**
 * Created by ell on 1/11/16.
 */
class DeviceHolder(itemView: View, private val listener: OnDeviceClickedListener?) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView
    private val urlTextView: TextView

    init {
        nameTextView = itemView.findViewById<View>(R.id.tv_name) as TextView
        urlTextView = itemView.findViewById<View>(R.id.tv_subtitle) as TextView
    }

    fun displayDevice(device: Device?) {
        if (device != null) {
            this.itemView.setOnClickListener { listener?.onDeviceClicked(device) }
            display(device)
        }
    }

    private fun display(device: Device) {
        nameTextView.text = device.name
        urlTextView.text = device.describer
    }
}
