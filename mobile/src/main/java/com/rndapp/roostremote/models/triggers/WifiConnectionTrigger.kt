package com.rndapp.roostremote.models.triggers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class WifiConnectionTrigger(flowName: String,
                            var wifiSsid: String = "Turin",
                            enabled: Boolean = false):
    Trigger(flowName, wifiSsid, enabled) {
    companion object {
        const val typeDescription = "Wifi Connection"
    }

    fun toggleEnabled(context: Context?) {
        if (context != null) {
//            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//            val intent = Intent(context, AlarmReceiver::class.java)
//            intent.putExtra(AlarmReceiver.flowIdentifierKey, flowName)
//            intent.putExtra(AlarmReceiver.alarmHashCode, hashCode())
//
//            val alarmIntent = PendingIntent.getBroadcast(context, hashCode(), intent, 0)
//
//            alarmMgr.cancel(alarmIntent)
//
//            if (!enabled) {
//                val date = nextTime(timeOfDay)
//                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.time, alarmIntent)
//            }
            enabled = !enabled
        }
    }

    override fun hashCode(): Int {
        return "$flowName$wifiSsid".hashCode()
    }
}