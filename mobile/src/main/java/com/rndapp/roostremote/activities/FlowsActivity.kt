package com.rndapp.roostremote.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.rndapp.roostremote.R
import com.rndapp.roostremote.adapters.FlowAdapter
import com.rndapp.roostremote.interfaces.OnItemClickedListener
import com.rndapp.roostremote.interfaces.OnItemLongClickedListener
import com.rndapp.roostremote.models.Flow
import kotlinx.android.synthetic.main.activity_edit_list.*

class FlowsActivity: AppCompatActivity() {
    var flows: ArrayList<Flow> = ArrayList()
    val adapter: FlowAdapter = FlowAdapter(flows, object: OnItemClickedListener {
        override fun onViewHolderClicked(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            flows[position].executeTasks()
        }
    }, object: OnItemLongClickedListener {
        override fun onViewHolderLongClicked(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
            val intent = Intent(this@FlowsActivity, CreateFlowActivity::class.java)
            intent.putExtra("flow", flows[position])
            startActivity(intent)
            flows.removeAt(position)
            Flow.setFlows(flows, this@FlowsActivity)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_list)

        supportActionBar?.title = "Batches"

        nameEditText.visibility = View.GONE
        tabs.visibility = View.GONE

        val layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        addFloatingActionButton.setOnClickListener {
            startActivity(Intent(this@FlowsActivity, CreateFlowActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        val allFlows = Flow.getFlows(this)
        if (allFlows != null) {
            flows.clear()
            flows.addAll(allFlows)
            adapter.notifyDataSetChanged()
        }
    }
}