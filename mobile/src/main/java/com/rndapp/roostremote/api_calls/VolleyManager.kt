package com.rndapp.roostremote.api_calls

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleyManager {
    companion object {
        var queue: RequestQueue? = null

        fun init(context: Context) {
            if (queue == null) queue = Volley.newRequestQueue(context)
        }
    }
}