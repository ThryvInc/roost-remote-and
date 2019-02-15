package com.rndapp.roostremote.api_calls

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rndapp.roostremote.Constants
import com.rndapp.roostremote.models.Device
import com.rndapp.roostremote.models.Option
import com.rndapp.roostremote.models.ServerDescription

public class GetDeviceTypeCall: GetDeviceCall() {
    companion object {
        fun addRequestToQueue(context: Context, deviceTypeId: String, queue: RequestQueue,
                                     callback: (ServerDescription) -> Unit, errorListener: Response.ErrorListener) {
            val url = Constants.USER_URL + "device_types/" + deviceTypeId

            val request = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
                val gson = GsonBuilder().registerTypeAdapter(Option::class.java, Option.OptionDeserializer()).create()
                val serverDescription = gson.fromJson(response.toString(), ServerDescription::class.java)
                callback(serverDescription)
            }, errorListener) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    return AuthenticatedCall.getHeaders(context)
                }
            }
            queue.add(request)
        }
    }
}