package com.rndapp.roostremote.adapters

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.rndapp.roostremote.R
import com.rndapp.roostremote.models.Option

import java.util.ArrayList

/**
 * Created by ell on 5/17/15.
 */
class ActionAdapter(context: Context, val options: ArrayList<Option>) : BaseAdapter() {
    private val mLayoutInflator: LayoutInflater

    init {
        mLayoutInflator = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return options.size
    }

    override fun getItem(position: Int): Option {
        return options[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = mLayoutInflator.inflate(R.layout.action_item, parent, false)
        }

        val option = getItem(position)
        (convertView!!.findViewById<View>(R.id.tv_action_name) as TextView).text = option.name

        return convertView
    }
}
