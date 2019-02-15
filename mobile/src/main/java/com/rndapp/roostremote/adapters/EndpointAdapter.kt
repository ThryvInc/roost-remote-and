package com.rndapp.roostremote.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.rndapp.roostremote.R
import com.rndapp.roostremote.models.Device
import com.rndapp.roostremote.models.Endpoint
import com.rndapp.roostremote.models.Option
import com.rndapp.roostremote.models.ServerDescription

/**
 * Created by ell on 4/29/15.
 */
class EndpointAdapter(internal var device: Device,
                      internal var description: ServerDescription,
                      val chosenCallback: (Endpoint?, Option?) -> Unit)
    : RecyclerView.Adapter<EndpointAdapter.EndpointViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): EndpointViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.card_view, viewGroup, false)
        return EndpointViewHolder(view)
    }

    override fun onBindViewHolder(endpointViewHolder: EndpointViewHolder, i: Int) {
        endpointViewHolder.setEndpoint(description.endpoints!![i])
    }

    override fun getItemCount(): Int {
        return description.endpoints!!.size
    }

    inner class EndpointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var nameTextView: TextView
        internal var endpointTextView: TextView
        internal var endpoint: Endpoint? = null

        init {
            nameTextView = itemView.findViewById<View>(R.id.tv_name) as TextView
            endpointTextView = itemView.findViewById<View>(R.id.tv_subtitle) as TextView
            itemView.setOnClickListener(this)
        }

        fun setEndpoint(endpoint: Endpoint) {
            this.endpoint = endpoint
            nameTextView.text = endpoint.name
            endpointTextView.text = endpoint.endpoint
        }

        override fun onClick(v: View) {
            val endpoint = endpoint
            if (endpoint != null) {
                if (endpoint.optionsHolder?.values?.size == 1) {
                    executeEndpoint(0)
                } else {
                    val builder = AlertDialog.Builder(v.context)
                    builder.setTitle("Choose an Action")
                    builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }

                    val adapter = ActionAdapter(v.context, endpoint.optionsHolder!!.values!!)
                    builder.setAdapter(adapter) { dialog, which -> executeEndpoint(which) }
                    builder.show()
                }
            }
        }

        private fun executeEndpoint(which: Int) {
            chosenCallback(endpoint, endpoint?.optionsHolder!!.values!![which])
        }
    }
}
