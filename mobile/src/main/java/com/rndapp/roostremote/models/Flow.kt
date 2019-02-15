package com.rndapp.roostremote.models

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rndapp.roostremote.models.tasks.Task
import java.io.Serializable
import com.google.gson.reflect.TypeToken
import com.rndapp.roostremote.models.triggers.Trigger

class Flow(var name: String, var tasks: List<Task>, var triggers: List<Trigger>? = null): Serializable {
    companion object {
        var flows: List<Flow>? = null

        fun refreshFlows(context: Context?) {
            if (context != null) {
                val flowsString = PrefsWrapper.getString("flows", context)
                val listType = object : TypeToken<List<Flow>>() {}.type
                val builder = GsonBuilder()
                builder.registerTypeAdapter(Task::class.java, Task.TaskDeserializer())
                builder.registerTypeAdapter(Trigger::class.java, Trigger.TriggerDeserializer())
                flows = builder.create().fromJson(flowsString, listType)
            }
        }

        fun getFlows(context: Context): List<Flow>? {
            refreshFlows(context)
            return flows
        }

        fun setFlows(flows: List<Flow>, context: Context) {
            val builder = GsonBuilder()
            builder.registerTypeAdapter(Task::class.java, Task.TaskSerializer())
            builder.registerTypeAdapter(Trigger::class.java, Trigger.TriggerSerializer())
            PrefsWrapper.saveString("flows", builder.create().toJson(flows), context)

            this.flows = flows
        }

        fun saveFlows(context: Context) {
            val flows = this.flows
            if (flows != null) {
                setFlows(flows, context)
            }
        }

        fun flowNamed(flowName: String): Flow? {
            return flows?.filter { it.name == flowName }?.firstOrNull()
        }

        fun triggerFlowNamed(flowName: String) {
            flows?.filter { it.name == flowName }?.firstOrNull()?.executeTasks()
        }

        fun triggerFlowNamed(flowName: String, callback: () -> Unit) {
            flows?.filter { it.name == flowName }?.firstOrNull()?.executeTasks(callback)
        }
    }

    fun executeTasks() {
        executeTaskNumber(0, null)
    }

    fun executeTasks(callback: () -> Unit) {
        executeTaskNumber(0, callback)
    }

    private fun executeTaskNumber(index: Int, callback: (() -> Unit)?) {
        if (index < tasks.size) {
            tasks[index].execute {
                executeTaskNumber(index + 1, callback)
            }
        } else {
            callback?.invoke()
        }
    }
}

class PrefsWrapper {
    companion object {
        fun saveString(key: String, string: String, context: Context?) {
            context?.getSharedPreferences("", Activity.MODE_PRIVATE)?.edit()?.putString(key, string)?.apply()
        }

        fun getString(key: String, context: Context?): String? {
            val prefs = context?.getSharedPreferences("", Activity.MODE_PRIVATE)
            return prefs?.getString(key, "")
        }
    }
}
