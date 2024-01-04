package com.rndapp.roostremote.models.triggers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rndapp.roostremote.api_calls.GetSunTimesCall
import com.rndapp.roostremote.api_calls.VolleyManager
import com.thryvinc.thux.models.isoDateFormatter
import com.thryvinc.thux.models.isoPlusFormatter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class SunsetTrigger(flowName: String,
                    var sunAction: String = "Sunrise/Sunset",
                    daysOfWeek: ArrayList<String> = ArrayList(),
                    enabled: Boolean = false):
    AlarmTrigger(flowName, sunAction, daysOfWeek, enabled) {
    companion object {
        const val typeDescription = "Sunrise/Sunset"
    }

    override fun toggleEnabled(context: Context?) {
        if (context != null) {
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(AlarmReceiver.flowIdentifierKey, flowName)
            intent.putExtra(AlarmReceiver.alarmHashCode, hashCode())

            val alarmIntent = PendingIntent.getBroadcast(context, hashCode(), intent, 0)

            alarmMgr.cancel(alarmIntent)

            if (!enabled) {
                GetSunTimesCall().addRequestToQueue(VolleyManager.queue!!, { todaySunTimes, tomorrowSunTimes ->
                    if (todaySunTimes != null) {
                        val fudgeFactor = 75.minutes
                        var suntime = if (sunAction == "Sunrise") todaySunTimes.sunrise else todaySunTimes.sunset
                        var date = fudgeFactor.before(isoPlusFormatter().parse(suntime))
                        if (date.before(now())) {
                            if (tomorrowSunTimes != null) {
                                suntime = if (sunAction == "Sunrise") tomorrowSunTimes.sunrise else tomorrowSunTimes.sunset
                                date = fudgeFactor.before(isoPlusFormatter().parse(suntime))
                            }
                        }
                        timeOfDay = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(date)

                        setAlarmAt(alarmMgr, alarmIntent)
                    }
                }) { error ->
                    error.printStackTrace()
                }
            }

            enabled = !enabled
        }
    }

    override fun hashCode(): Int {
        println("sunset hash")
        return "$flowName$sunAction".hashCode()
    }
}