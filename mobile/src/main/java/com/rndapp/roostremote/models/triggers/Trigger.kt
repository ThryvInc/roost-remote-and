package com.rndapp.roostremote.models.triggers

import com.google.gson.*
import com.rndapp.roostremote.models.Option
import java.io.Serializable
import java.lang.reflect.Type

abstract class Trigger(val flowName: String, val name: String, var enabled: Boolean = false): Serializable {

    class TriggerSerializer : JsonSerializer<Trigger> {
        override fun serialize(src: Trigger?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            if (src != null && context != null) {
                if (src is SunsetTrigger) {
                    return context.serialize(src)
                } else if (src is AlarmTrigger) {
                    return context.serialize(src)
                }
            }
            return JsonPrimitive("")
        }
    }

    class TriggerDeserializer : JsonDeserializer<Trigger> {

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Trigger? {
            val jsonObject = json.asJsonObject

            val sunset = trySunsetTrigger(jsonObject)
            if (sunset != null && sunset.sunAction != null) {
                return sunset
            }

            val alarm = tryAlarmTrigger(jsonObject)
            if (alarm != null) {
                return alarm
            }

            val wifi = tryWifiTrigger(jsonObject)
            if (wifi != null) {
                return wifi
            }

            return null
        }

        fun tryAlarmTrigger(jsonObject: JsonObject): AlarmTrigger? {
            return Gson().fromJson(jsonObject.toString(), AlarmTrigger::class.java)
        }

        fun tryWifiTrigger(jsonObject: JsonObject): WifiConnectionTrigger? {
            return Gson().fromJson(jsonObject.toString(), WifiConnectionTrigger::class.java)
        }

        fun trySunsetTrigger(jsonObject: JsonObject): SunsetTrigger? {
            return Gson().fromJson(jsonObject.toString(), SunsetTrigger::class.java)
        }
    }
}
