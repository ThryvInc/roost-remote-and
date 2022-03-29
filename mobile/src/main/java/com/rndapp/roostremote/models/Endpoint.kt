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
import com.android.volley.VolleyLog
import com.android.volley.AuthFailureError
import com.android.volley.Request.Method.PUT
import com.android.volley.Request.Method.GET
import com.android.volley.Request.Method.POST



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
        val url: String
        if (description.hostNamespace?.startsWith("/") == true) {
            url = Constants.DESCRIBER_SCHEME + device.host + description.hostNamespace + endpoint
        } else {
            url = Constants.DESCRIBER_SCHEME + device.host + "/" + description.hostNamespace + endpoint
        }
        try {
            val `object` = option.getJsonObject(optionsHolder!!.key!!)
            Option.addStaticValues(`object`, optionsHolder!!.staticValues)
            Log.d("object", `object`.toString())
            val request = object : JsonObjectRequest(methodStringToInt(),
                    url,
                    `object`,
                    Response.Listener { jsonObject ->
                        Log.d("response", jsonObject.toString())
                        if (callback != null) {
                            callback()
                        }
                    }, errorListener) {

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
            logToCurlRequest(request)
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

    private fun logToCurlRequest(request: Request<*>) {
        val builder = StringBuilder()
        builder.append("curl request: curl ")
        builder.append("-X \"")
        when (request.method) {
            Request.Method.POST -> builder.append("POST")
            Request.Method.GET -> builder.append("GET")
            Request.Method.PUT -> builder.append("PUT")
            Request.Method.DELETE -> builder.append("DELETE")
        }
        builder.append("\"")

        try {
            if (request.body != null) {
                builder.append(" -D ")
                var data = String(request.body)
                data = data.replace("\"".toRegex(), "\\\"")
                builder.append("\"")
                builder.append(data)
                builder.append("\"")
            }
            for (key in request.headers.keys) {
                builder.append(" -H '")
                builder.append(key)
                builder.append(": ")
                builder.append(request.headers[key])
                builder.append("'")
            }
            builder.append(" \"")
            builder.append(request.url)
            builder.append("\"")

            Log.d("volley", builder.toString())
        } catch (e: AuthFailureError) {
            Log.wtf("vollye","Unable to get body of response or headers for curl logging")
        }
    }
}


