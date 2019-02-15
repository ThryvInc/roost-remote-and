package com.rndapp.roostremote.models

import com.google.gson.Gson

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ell on 1/4/17.
 */

class MapOption(name: String, value: Map<*, *>) : Option(name, value) {

    @Throws(JSONException::class)
    override fun getJsonObject(key: String): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(key, JSONObject(Gson().toJson(value as Map<*, *>)))
        return jsonObject
    }
}
