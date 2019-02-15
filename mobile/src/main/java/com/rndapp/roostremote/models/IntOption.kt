package com.rndapp.roostremote.models

import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ell on 5/16/15.
 */
class IntOption(name: String, value: Int) : Option(name, value) {

    @Throws(JSONException::class)
    override fun getJsonObject(key: String): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put(key, value as Int)
        return jsonObject
    }
}
