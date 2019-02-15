package com.rndapp.roostremote.models

import android.util.Log

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ell on 5/16/15.
 */
class BoolOption(name: String, value: Boolean) : Option(name, value) {

    @Throws(JSONException::class)
    override fun getJsonObject(key: String): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(key, value as Boolean)
        Log.d("bool opt", jsonObject.toString())
        return jsonObject
    }
}
