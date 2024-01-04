package com.rndapp.roostremote.models.triggers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.rndapp.roostremote.api_calls.GetSunTimesCall
import com.rndapp.roostremote.api_calls.VolleyManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class AlarmTrigger(flowName: String,
                   var timeOfDay: String = "6:00 AM",
                   val daysOfWeek: ArrayList<String> = ArrayList(),
                   enabled: Boolean = false):
        Trigger(flowName, timeOfDay, enabled) {
    companion object {
        const val typeDescription = "Alarm"
    }

    open fun toggleEnabled(context: Context?) {
        if (context != null) {
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra(AlarmReceiver.flowIdentifierKey, flowName)
            intent.putExtra(AlarmReceiver.alarmHashCode, hashCode())

            val alarmIntent = PendingIntent.getBroadcast(context, hashCode(), intent, 0)

            alarmMgr.cancel(alarmIntent)

            if (!enabled) {
                GetSunTimesCall().addRequestToQueue(VolleyManager.queue!!, { todaySunTimes, tomorrowSunTimes ->
                setAlarmAt(alarmMgr, alarmIntent)
                }) { error ->
                    error.printStackTrace()
                }
            }
            enabled = !enabled
        }
    }

    fun setAlarmAt(alarmMgr: AlarmManager, alarmIntent: PendingIntent) {
        val date = nextTime(timeOfDay)
        if (daysOfWeek.isEmpty()) {
            alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, date.time, alarmIntent)
        } else {
            for (i in 0..7) {
                val day = SimpleDateFormat("EEEE", Locale.US).format(i.days.after(date))
                if (daysOfWeek.contains(day)) {
                    println("added: $date")
                    alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, i.days.after(date).time, alarmIntent)
                    break
                }
            }
        }
    }

    override fun hashCode(): Int {
        return "$flowName$timeOfDay".hashCode()
    }
}

internal val calendar: Calendar
    get() {
        return Calendar.getInstance()
    }

fun nextTime(dateString: String): Date {
    val cal = Calendar.getInstance()
    val timeFormatter: DateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
    var date = timeFormatter.parse(dateString)
    cal.set(Calendar.HOUR_OF_DAY, date.hours)
    cal.set(Calendar.MINUTE, date.minutes)
    cal.set(Calendar.SECOND, 0)
    date = cal.time
    if (date isBefore now()) date = 1.day.after(date)
    return date
}

infix fun Date.isAfter(date: Date) = this.after(date)
infix fun Date.isBefore(date: Date) = this.before(date)

fun now(): Date = calendar.time

fun tomorrow(): Date = 1.day.after(now())

val Int.day: Duration
    get() = Duration(unit = Calendar.DAY_OF_YEAR, value = this)

val Int.days: Duration
    get() = day

val Int.hour: Duration
    get() = Duration(unit = Calendar.HOUR_OF_DAY, value = this)

val Int.hours: Duration
    get() = hour

val Int.minute: Duration
    get() = Duration(unit = Calendar.MINUTE, value = this)

val Int.minutes: Duration
    get() = minute

val Int.second: Duration
    get() = Duration(unit = Calendar.SECOND, value = this)

val Int.seconds: Duration
    get() = second

class Duration(internal val unit: Int, internal val value: Int) {
    val ago = calculate(from = now(), value = -value)

    val since = calculate(from = now(), value = value)
    val hence = since
    val fromNow = since

    fun after(date: Date) = calculate(date, value)
    fun before(date: Date) = calculate(date, -value)

    private fun calculate(from: Date, value: Int): Date {
        val calendar = calendar
        calendar.time = from
        calendar.add(unit, value)
        return calendar.time
    }

//    override fun hashCode() = Objects.hashCode(unit) * Objects.hashCode(value)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Duration) {
            return false
        }
        return unit == other.unit && value == other.value
    }
}
