package com.rndapp.roostremote.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.rndapp.roostremote.R
import com.rndapp.roostremote.models.triggers.AlarmTrigger
import com.rndapp.roostremote.models.triggers.SunsetTrigger
import com.thryvinc.thux.adapters.LayoutIdRecyclerItemViewModel
import com.thryvinc.thux.adapters.ModelRecyclerViewAdapter
import com.thryvinc.thux.adapters.ModelViewHolder

class TriggersAdapter(var triggers: ArrayList<AlarmTrigger>,
                      var deleteTrigger: (AlarmTrigger) -> Unit,
                      var editTriggerClicked: (AlarmTrigger) -> Unit):
        ModelRecyclerViewAdapter(triggers.map { AlarmTriggerItem(it, deleteTrigger, editTriggerClicked) }) {

    fun notifyChanged() {
        itemViewModels = triggers.map { AlarmTriggerItem(it, deleteTrigger, editTriggerClicked) }
        notifyDataSetChanged()
    }
}

class AlarmTriggerItem(trigger: AlarmTrigger,
                       val deleteTrigger: (AlarmTrigger) -> Unit,
                       val editTriggerTime: (AlarmTrigger) -> Unit):
        LayoutIdRecyclerItemViewModel<AlarmTrigger>(trigger, R.layout.item_alarm) {

    override fun configureHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        if (holder is AlarmTriggerViewHolder) {
            holder.configure(model, editTriggerTime, deleteTrigger)
        }
    }

    override fun viewHolderWithView(view: View): ModelViewHolder<AlarmTrigger> {
        return AlarmTriggerViewHolder(view)
    }
}

class AlarmTriggerViewHolder(itemView: View) : ModelViewHolder<AlarmTrigger>(itemView) {
    val nameTextView: TextView = itemView.findViewById(R.id.timeTextView)
    val enabledSwitch: Switch = itemView.findViewById(R.id.enabledSwitch)
    val deleteImageView: ImageView = itemView.findViewById(R.id.deleteImageView)

    val sundayTextView: TextView = itemView.findViewById(R.id.sunday)
    val mondayTextView: TextView = itemView.findViewById(R.id.monday)
    val tuesdayTextView: TextView = itemView.findViewById(R.id.tuesday)
    val wednesdayTextView: TextView = itemView.findViewById(R.id.wednesday)
    val thursdayTextView: TextView = itemView.findViewById(R.id.thursday)
    val fridayTextView: TextView = itemView.findViewById(R.id.friday)
    val saturdayTextView: TextView = itemView.findViewById(R.id.saturday)

    override fun configure(model: AlarmTrigger) {
        configure(model, null, null)
    }

    fun configure(
        model: AlarmTrigger,
        editTriggerTime: ((AlarmTrigger) -> Unit)?,
        deleteTrigger: ((AlarmTrigger) -> Unit)?
    ) {
        if (model is SunsetTrigger) {
            nameTextView.text = model.sunAction
        } else {
            nameTextView.text = model.timeOfDay
        }
        nameTextView.setOnClickListener { editTriggerTime?.invoke(model) }
        enabledSwitch.isChecked = model.enabled
        enabledSwitch.setOnCheckedChangeListener { button, isChecked ->
            model.toggleEnabled(itemView.context)
        }
        deleteImageView.setOnClickListener { deleteTrigger?.invoke(model) }

        setupWeek(model)
    }

    fun setupWeek(model: AlarmTrigger) {
        configureWeekDisplay(model)
        sundayTextView.setOnClickListener {
            if (model.daysOfWeek.contains("Sunday")) {
                model.daysOfWeek.remove("Sunday")
            } else {
                model.daysOfWeek.add("Sunday")
            }
            configureWeekDisplay(model)
        }
        mondayTextView.setOnClickListener {
            if (model.daysOfWeek.contains("Monday")) {
                model.daysOfWeek.remove("Monday")
            } else {
                model.daysOfWeek.add("Monday")
            }
            configureWeekDisplay(model)
        }
        tuesdayTextView.setOnClickListener {
            if (model.daysOfWeek.contains("Tuesday")) {
                model.daysOfWeek.remove("Tuesday")
            } else {
                model.daysOfWeek.add("Tuesday")
            }
            configureWeekDisplay(model)
        }
        wednesdayTextView.setOnClickListener {
            if (model.daysOfWeek.contains("Wednesday")) {
                model.daysOfWeek.remove("Wednesday")
            } else {
                model.daysOfWeek.add("Wednesday")
            }
            configureWeekDisplay(model)
        }
        thursdayTextView.setOnClickListener {
            if (model.daysOfWeek.contains("Thursday")) {
                model.daysOfWeek.remove("Thursday")
            } else {
                model.daysOfWeek.add("Thursday")
            }
            configureWeekDisplay(model)
        }
        fridayTextView.setOnClickListener {
            if (model.daysOfWeek.contains("Friday")) {
                model.daysOfWeek.remove("Friday")
            } else {
                model.daysOfWeek.add("Friday")
            }
            configureWeekDisplay(model)
        }
        saturdayTextView.setOnClickListener {
            if (model.daysOfWeek.contains("Saturday")) {
                model.daysOfWeek.remove("Saturday")
            } else {
                model.daysOfWeek.add("Saturday")
            }
            configureWeekDisplay(model)
        }
    }

    fun configureWeekDisplay(model: AlarmTrigger) {
        if (model.daysOfWeek.contains("Sunday")) {
            sundayTextView.setBackgroundResource(R.drawable.capped_accent_button)
        } else {
            sundayTextView.setBackgroundDrawable(null)
        }
        if (model.daysOfWeek.contains("Monday")) {
            mondayTextView.setBackgroundResource(R.drawable.capped_accent_button)
        } else {
            mondayTextView.setBackgroundDrawable(null)
        }
        if (model.daysOfWeek.contains("Tuesday")) {
            tuesdayTextView.setBackgroundResource(R.drawable.capped_accent_button)
        } else {
            tuesdayTextView.setBackgroundDrawable(null)
        }
        if (model.daysOfWeek.contains("Wednesday")) {
            wednesdayTextView.setBackgroundResource(R.drawable.capped_accent_button)
        } else {
            wednesdayTextView.setBackgroundDrawable(null)
        }
        if (model.daysOfWeek.contains("Thursday")) {
            thursdayTextView.setBackgroundResource(R.drawable.capped_accent_button)
        } else {
            thursdayTextView.setBackgroundDrawable(null)
        }
        if (model.daysOfWeek.contains("Friday")) {
            fridayTextView.setBackgroundResource(R.drawable.capped_accent_button)
        } else {
            fridayTextView.setBackgroundDrawable(null)
        }
        if (model.daysOfWeek.contains("Saturday")) {
            saturdayTextView.setBackgroundResource(R.drawable.capped_accent_button)
        } else {
            saturdayTextView.setBackgroundDrawable(null)
        }
    }
}
