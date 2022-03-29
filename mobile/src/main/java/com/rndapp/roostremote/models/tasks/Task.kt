package com.rndapp.roostremote.models.tasks

import com.google.gson.*
import com.rndapp.roostremote.models.Option
import java.io.Serializable
import java.lang.reflect.Type

abstract class Task(var name: String): Serializable {
    abstract fun execute(callback: () -> Unit)

    class TaskSerializer : JsonSerializer<Task> {
        override fun serialize(src: Task?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
            if (src != null && context != null) {
                if (src is WaitTask) {
                    return context.serialize(src)
                } else if (src is EndpointOptionTask) {
                    return context.serialize(src)
                } else if (src is FlowTask) {
                    return context.serialize(src)
                }
            }
            return JsonPrimitive("")
        }
    }

    class TaskDeserializer : JsonDeserializer<Task> {

        @Throws(JsonParseException::class)
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Task? {
            val jsonObject = json.asJsonObject
            return tryWaitTask(jsonObject)
        }

        fun tryWaitTask(jsonObject: JsonObject): Task? {
            val name = jsonObject.get("name").asString
            if (name.startsWith("Wait", true)) {
                try {
                    val delayJson = jsonObject.get("delay")
                    if (delayJson != null) {
                        val delayString = delayJson.asString
                        val delayValue = delayString.toLong()
                        return WaitTask(delayValue)
                    } else {
                        return null
                    }
                } catch (e: NumberFormatException) {
                    return tryEndpointTask(jsonObject)
                }
            } else if (jsonObject.has("device")) {
                return tryEndpointTask(jsonObject)
            } else {
                return tryFlowTask(jsonObject)
            }
        }

        fun tryEndpointTask(jsonObject: JsonObject): EndpointOptionTask? {
            try {
                val builder = GsonBuilder()
                builder.registerTypeAdapter(Option::class.java, Option.OptionDeserializer())
                return builder.create().fromJson(jsonObject, EndpointOptionTask::class.java)
            } catch (e: Exception) {
                return null
            }
        }

        fun tryFlowTask(jsonObject: JsonObject): FlowTask? {
            try {
                val builder = GsonBuilder()
                builder.registerTypeAdapter(Option::class.java, Option.OptionDeserializer())
                return builder.create().fromJson(jsonObject, FlowTask::class.java)
            } catch (e: Exception) {
                return null
            }
        }
    }
}
