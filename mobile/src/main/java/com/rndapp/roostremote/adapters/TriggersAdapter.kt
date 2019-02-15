package com.rndapp.roostremote.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.rndapp.roostremote.R
import com.rndapp.roostremote.models.triggers.AlarmTrigger
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

    override fun configureHolder(holder: RecyclerView.ViewHolder) {
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

    override fun configure(model: AlarmTrigger) {
        configure(model, null, null)
    }

    fun configure(model: AlarmTrigger, editTriggerTime: ((AlarmTrigger) -> Unit)?, deleteTrigger: ((AlarmTrigger) -> Unit)?) {
        nameTextView.text = model.timeOfDay
        nameTextView.setOnClickListener { editTriggerTime?.invoke(model) }
        enabledSwitch.isChecked = model.enabled
        enabledSwitch.setOnCheckedChangeListener { button, isChecked ->
            model.toggleEnabled(itemView.context)
        }
        deleteImageView.setOnClickListener { deleteTrigger?.invoke(model) }
    }
}
