package com.rndapp.roostremote.models

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.annotations.SerializedName
import com.rndapp.roostremote.Constants
import com.rndapp.roostremote.api_calls.VolleyManager
import java.io.Serializable

/**
 * Created by ell on 4/29/15.
 */
class Endpoint: Serializable {
    var name: String? = null
        protected set
    var method: String? = null
        protected set
    var endpoint: String? = null
        protected set
    @SerializedName("options")
    var optionsHolder: OptionsHolder? = null
        protected set

    fun execute(device: Device, description: ServerDescription, option: Option,
                callback: (() -> Unit)? = null, errorListener: Response.ErrorListener) {
        val url = Constants.DESCRIBER_SCHEME + device.host + "/" + description.hostNamespace + endpoint
        try {
            val `object` = option.getJsonObject(optionsHolder!!.key!!)
            Option.addStaticValues(`object`, optionsHolder!!.staticValues)
            Log.d("object", `object`.toString())
            val request = JsonObjectRequest(methodStringToInt(),
                    url,
                    `object`,
                    Response.Listener { jsonObject ->
                        Log.d("response", jsonObject.toString())
                        if (callback != null) {
                            callback()
                        }
                    }, errorListener)
            VolleyManager.queue?.add(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    protected fun methodStringToInt(): Int {
        if (method == "GET") return Request.Method.GET
        if (method == "PUT") return Request.Method.PUT
        if (method == "POST") return Request.Method.POST
        return if (method == "DELETE") Request.Method.DELETE else -1
    }
}
