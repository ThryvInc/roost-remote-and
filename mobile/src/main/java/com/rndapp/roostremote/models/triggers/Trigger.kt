package com.rndapp.roostremote.models.triggers

import com.google.gson.*
import com.rndapp.roostremote.models.Option
import java.io.Serializable
import java.lang.reflect.Type

abstract class Trigger(val flowName: String, val name: String, var enabled: Boolean = false): Serializable {

    class TriggerSerializer : JsonSerializer<Trigger> {
        override fun serialize(src: Trigger?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            if (src != null && context != null) {
                if (src is AlarmTrigger) {
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
            return tryAlarmTrigger(jsonObject)
        }

        fun tryAlarmTrigger(jsonObject: JsonObject): AlarmTrigger? {
            return Gson().fromJson(jsonObject.toString(), AlarmTrigger::class.java)
        }
    }

}