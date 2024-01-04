package com.rndapp.roostremote.api_calls

import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.rndapp.roostremote.models.SunResults
import com.rndapp.roostremote.models.SunTimes
import com.rndapp.roostremote.models.triggers.now
import com.rndapp.roostremote.models.triggers.tomorrow

class GetSunTimesCall {

    interface SunListener {
        fun onSunTimesParsed(todaySunTimes: SunTimes?, tomorrowSunTimes: SunTimes?)
    }

    fun addRequestToQueue(
        queue: RequestQueue,
        listener: (SunTimes?, SunTimes?) -> Unit,
        errorListener: Response.ErrorListener?
    ) {
        val today = now().toString()
        val tomorrow = tomorrow().toString()
        val url = "https://api.sunrise-sunset.org/json?lat=42.29077&lng=-71.13895&formatted=0&date="
        val request = JsonObjectRequest(url + today,
            Response.Listener { response ->
                Log.d("response1", response.toString())
                val results = Gson().fromJson(response.toString(), SunResults::class.java)
                val tomorrowRequest = JsonObjectRequest(url + tomorrow,
                    Response.Listener { response ->
                        Log.d("response2", response.toString())
                        val tomorrowResults = Gson().fromJson(response.toString(), SunResults::class.java)
                        listener(results.results, tomorrowResults.results)
                    }, errorListener)
                queue.add(tomorrowRequest)
            }, errorListener)
        queue.add(request)
    }
}