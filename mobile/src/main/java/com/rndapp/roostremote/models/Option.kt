package com.rndapp.roostremote.models

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken

import org.json.JSONException
import org.json.JSONObject

import java.io.Serializable
import java.lang.reflect.Type
import java.util.HashMap

/**
 * Created by ell on 5/17/15.
 */
abstract class Option(val name: String,
                      val value: Any) : Serializable {
    @Throws(JSONException::class)
    abstract fun getJsonObject(key: String): JSONObject

    class OptionDeserializer : JsonDeserializer<Option> {

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Option {
            val jsonObject = json.asJsonObject
            return tryIntOption(jsonObject)
        }

        private fun tryIntOption(jsonObject: JsonObject): Option {
            val name = jsonObject.get("name").asString
            try {
                val intString = jsonObject.get("value").asString
                val intValue = Integer.parseInt(intString)
                return IntOption(name, intValue)
            } catch (e: NumberFormatException) {
                return tryStringOption(jsonObject)
            } catch (ue: UnsupportedOperationException) {
                return tryMapOption(jsonObject)
            }

        }

        private fun tryMapOption(jsonObject: JsonObject): Option {
            val name = jsonObject.get("name").asString
            val map = Gson().fromJson<Map<String, Any>>(jsonObject.get("value").toString(),
                    object : TypeToken<HashMap<String, Any>>() {

                    }.type) ?: return tryStringOption(jsonObject)
            return MapOption(name, map)
        }

        private fun tryStringOption(jsonObject: JsonObject): Option {
            val name = jsonObject.get("name").asString
            try {
                val stringValue = jsonObject.get("value").asString
                if (stringValue == "true" || stringValue == "false") {
                    val boolValue = jsonObject.get("value").asBoolean
                    return BoolOption(name, boolValue)
                }
                return StringOption(name, stringValue)
            } catch (e2: Exception) {
                e2.printStackTrace()
                return tryBoolOption(jsonObject)
            }

        }

        private fun tryBoolOption(jsonObject: JsonObject): Option {
            val name = jsonObject.get("name").asString
            try {
                val boolValue = jsonObject.get("value").asBoolean
                return BoolOption(name, boolValue)
            } catch (e1: Exception) {
                return tryStringOption(jsonObject)
            }

        }

    }

    companion object {
        fun addStaticValues(jsonObject: JSONObject?, staticValues: List<Option>?) {
            if (jsonObject != null) {
                if (staticValues != null) {
                    try {
                        for (value in staticValues) {
                            jsonObject.put(value.name, value.value)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }
}
