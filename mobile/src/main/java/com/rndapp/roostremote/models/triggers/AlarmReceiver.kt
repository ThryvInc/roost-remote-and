package com.rndapp.roostremote.models.triggers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.widget.Toast
import com.rndapp.roostremote.models.Flow

class AlarmReceiver: BroadcastReceiver() {
    companion object {
        const val flowIdentifierKey: String = "flowIdentifierKey"
        const val alarmHashCode: String = "alarmHashCode"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "roostremote: alarm receiver")
            wl.acquire(100)

            val name = intent.extras?.getString(flowIdentifierKey)
            val hashCode = intent.extras?.getInt(alarmHashCode)
            if (name != null && name.isNotBlank()) {
                Flow.refreshFlows(context)
                val flow = Flow.flowNamed(name)
                val trigger = flow?.triggers?.filter { it.hashCode() == hashCode }?.firstOrNull()
                if (trigger != null && trigger.enabled) {
                    flow.executeTasks()
                }
                trigger?.enabled = false

                Flow.saveFlows(context)

                Toast.makeText(context, "Alarm: $name", Toast.LENGTH_LONG).show()
            }

            wl.release()
        }
    }
}