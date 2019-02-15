package com.rndapp.roostremote.models

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ell on 1/3/17.
 */

class StringOption(name: String, value: String) : Option(name, value) {

    @Throws(JSONException::class)
    override fun getJsonObject(key: String): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(key, value as String)
        return jsonObject
    }
}
