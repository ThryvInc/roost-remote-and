package com.rndapp.roostremote.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rndapp.roostremote.R
import com.rndapp.roostremote.interfaces.OnDeviceClickedListener
import com.rndapp.roostremote.models.Device
import com.rndapp.roostremote.models.DeviceHolder

/**
 * Created by ell on 1/11/16.
 */
class DeviceAdapter(private val devices: List<Device>?, private val listener: OnDeviceClickedListener) : RecyclerView.Adapter<DeviceHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.card_view, parent, false)
        return DeviceHolder(view, listener)
    }

    override fun onBindViewHolder(holder: DeviceHolder, position: Int) {
        holder.displayDevice(devices!![position])
    }

    override fun getItemCount(): Int {
        return devices?.size ?: 0
    }
}
