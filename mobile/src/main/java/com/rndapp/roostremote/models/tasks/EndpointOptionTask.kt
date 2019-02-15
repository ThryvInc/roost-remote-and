package com.rndapp.roostremote.models.tasks

import com.android.volley.Response
import com.rndapp.roostremote.models.Device
import com.rndapp.roostremote.models.Endpoint
import com.rndapp.roostremote.models.Option
import com.rndapp.roostremote.models.ServerDescription

class EndpointOptionTask(val device: Device, val description: ServerDescription,
                         val endpoint: Endpoint, val option: Option):
        Task("${device.name ?: ""} > ${endpoint.name ?: ""} > ${option.name}") {
    companion object {
        const val typeDescription = "Make a device do something"
    }

    override fun execute(callback: () -> Unit) {
        endpoint.execute(device, description, option, callback, Response.ErrorListener {
            callback()
        })
    }
}
