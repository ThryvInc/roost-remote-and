package com.rndapp.roostremote.activities

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import com.rndapp.roostremote.Constants
import com.rndapp.roostremote.R
import com.rndapp.roostremote.adapters.TaskAdapter
import com.rndapp.roostremote.adapters.TriggersAdapter
import com.rndapp.roostremote.api_calls.GetDeviceTypeCall
import com.rndapp.roostremote.api_calls.GetDevicesCall
import com.rndapp.roostremote.api_calls.GetPlacesCall
import com.rndapp.roostremote.api_calls.VolleyManager
import com.rndapp.roostremote.interfaces.OnItemClickedListener
import com.rndapp.roostremote.models.*
import com.rndapp.roostremote.models.tasks.EndpointOptionTask
import com.rndapp.roostremote.models.tasks.FlowTask
import com.rndapp.roostremote.models.tasks.Task
import com.rndapp.roostremote.models.tasks.WaitTask
import com.rndapp.roostremote.models.triggers.AlarmTrigger
import com.rndapp.roostremote.models.triggers.Trigger
import kotlinx.android.synthetic.main.activity_edit_list.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateFlowActivity: AppCompatActivity(), TimePickerDialog.OnTimeSetListener {
    var devices = ArrayList<Device>()
    var device: Device? = null
    var deviceDescription: ServerDescription? = null
    var tasks = ArrayList<Task>()
    var tasksAdapter = TaskAdapter(tasks, object: OnItemClickedListener {
        override fun onViewHolderClicked(holder: RecyclerView.ViewHolder, position: Int) {
            deleteTask(position)
        }
    })
    var triggersAdapter = TriggersAdapter(ArrayList(), this::deleteTrigger, this::editTrigger)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_list)

        val flow = intent.extras?.getSerializable("flow") as? Flow
        if (flow != null) {
            nameEditText.setText(flow.name)
            tasks.addAll(flow.tasks)
            tasksAdapter.notifyDataSetChanged()

            val flowTriggers = flow.triggers
            if (flowTriggers != null) {
                val alarmTriggers = flowTriggers.filter { it is AlarmTrigger } as List<AlarmTrigger>
                triggersAdapter.triggers.addAll(alarmTriggers)
                triggersAdapter.notifyChanged()
            }
        }

        supportActionBar?.title = "New Batch"

        val layoutManager = LinearLayoutManager(this@CreateFlowActivity)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = tasksAdapter

        GetPlacesCall.addRequestToQueue(this@CreateFlowActivity, VolleyManager.queue, { places ->
            GetDevicesCall.addRequestToQueue(this, places[0]!!.id, VolleyManager.queue, { devices ->
                this@CreateFlowActivity.devices = ArrayList(devices)
            }) { error -> Toast.makeText(this@CreateFlowActivity, error.message, Toast.LENGTH_SHORT).show() }
        }) { error ->
            Toast.makeText(this@CreateFlowActivity, error.message, Toast.LENGTH_SHORT).show()
        }

        tabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    recyclerView.adapter = tasksAdapter
                } else {
                    recyclerView.adapter = triggersAdapter
                }
            }
        })

        addFloatingActionButton.setOnClickListener {
            if (tabs.selectedTabPosition == 0) {
                val titles = arrayOf(WaitTask.typeDescription, EndpointOptionTask.typeDescription, FlowTask.typeDescription)
                promptList(titles, "Choose a task type") { which ->
                    when (titles[which]) {
                        WaitTask.typeDescription -> createWaitTask()
                        EndpointOptionTask.typeDescription -> createEndpointTask()
                        FlowTask.typeDescription -> createFlowTask()
                    }
                }
            } else {
                val titles = arrayOf(AlarmTrigger.typeDescription)
                promptList(titles, "Choose a trigger type") { which ->
                    when (titles[which]) {
                        AlarmTrigger.typeDescription -> createAlarmTrigger()
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        val name = nameEditText.text.toString()
        if (name.isNotBlank()) {
            val flow = Flow(name, tasks, triggersAdapter.triggers)
            val allFlows = Flow.getFlows(this)?.filter { it.name != name } ?: ArrayList()
            val newFlows = ArrayList(allFlows)
            newFlows.add(flow)
            Flow.setFlows(newFlows, this)
        }
    }

    fun createWaitTask() {
        val alertDialogBuilder = AlertDialog.Builder(this@CreateFlowActivity)
        // set title
        alertDialogBuilder.setTitle("New Wait Task")

        val layout = this.layoutInflater.inflate(R.layout.new_wait_task, null)

        val delayEditText = layout.findViewById<EditText>(R.id.delayEditText)

        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setView(layout)
                .setPositiveButton("Ok",
                        DialogInterface.OnClickListener { dialog, id ->
                            val delay = delayEditText.text.toString().toLong()
                            val task = WaitTask(delay)
                            tasks.add(task)
                            tasksAdapter.notifyDataSetChanged()
                            dialog.dismiss()
                        })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id -> })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun createEndpointTask() {
        val names = ArrayList<String>(devices.map { it.name } )
        val titles = Array(names.size, { i -> names[i] })
        promptList(titles, "Which device?") { which ->
            device = devices[which]
            fetchEndpoints(devices[which])
        }
    }

    fun createFlowTask() {
        val names = ArrayList<String>(Flow.flows?.map { it.name })
        val titles = Array(names.size) { i -> names[i] }
        promptList(titles, "Which batch?") { which ->
            val name = titles[which]
            tasks.add(FlowTask(name))
            tasksAdapter.notifyDataSetChanged()
        }
    }

    fun createAlarmTrigger() {
        val cal = Calendar.getInstance()
        TimePickerDialog(this, this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }

    override fun onTimeSet(picker: TimePicker?, hourOfDay: Int, minute: Int) {
        val cal = Calendar.getInstance()

        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val dayOfMonth: Int = cal.get(Calendar.DAY_OF_MONTH)

        cal.set(year, month, dayOfMonth, hourOfDay, minute)

        val timeFormatter: DateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

        val name = nameEditText.text.toString()
        if (name.isNotBlank()) {
            val alarmTrigger = AlarmTrigger(name, timeFormatter.format(cal.time))
            alarmTrigger.toggleEnabled(this)
            triggersAdapter.triggers.add(alarmTrigger)
            triggersAdapter.notifyChanged()
        }
    }

    fun fetchEndpoints(device: Device) {
        val deviceTypeId = device.deviceTypeId
        if (deviceTypeId != null) {
            GetDeviceTypeCall.addRequestToQueue(this, deviceTypeId, Volley.newRequestQueue(this), { serverDescription ->
                deviceDescription = serverDescription
                promptEndpoint(serverDescription)
            }, Response.ErrorListener { volleyError ->
                volleyError.printStackTrace()
                Log.e("error", volleyError.localizedMessage ?: "")
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_SHORT).show()
            })
        } else {
            val request = JsonObjectRequest(
                    Constants.DESCRIBER_SCHEME + device.describer + "/" + device.describerNamespace + "/index", null,
                    Response.Listener { jsonObject ->
                        val gson = GsonBuilder().registerTypeAdapter(Option::class.java, Option.OptionDeserializer()).create()
                        val serverDescription = gson.fromJson(jsonObject.toString(), ServerDescription::class.java)
                        if (serverDescription != null) {
                            deviceDescription = serverDescription
                            promptEndpoint(serverDescription)
                        } else {
                            Toast.makeText(applicationContext, "o noes", Toast.LENGTH_SHORT).show()
                        }
                    }, Response.ErrorListener { volleyError ->
                volleyError.printStackTrace()
                Log.e("error", volleyError.localizedMessage ?: "")
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_SHORT).show()
            })
            request.setShouldCache(false)
            Volley.newRequestQueue(this).add(request)
        }
    }

    fun promptEndpoint(serverDescription: ServerDescription) {
        val endpoints = serverDescription.endpoints
        if (endpoints != null) {
            val names = ArrayList<String>(endpoints.map { it.name } )
            val titles = Array(names.size, { i -> names[i] })
            promptList(titles, "Which endpoint?") { which ->
                promptOption(endpoints[which])
            }
        }
    }

    fun promptOption(endpoint: Endpoint) {
        val options = endpoint.optionsHolder?.values
        if (options != null) {
            if (options.size == 1) {
                val task = EndpointOptionTask(device!!, deviceDescription!!, endpoint, options[0])
                tasks.add(task)
                tasksAdapter.notifyDataSetChanged()
            } else {
                val names = ArrayList<String>(options.map { it.name } )
                val titles = Array(names.size) { i -> names[i] }
                promptList(titles, "Which action?") { which ->
                    val task = EndpointOptionTask(device!!, deviceDescription!!, endpoint, options[which])
                    tasks.add(task)
                    tasksAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun promptList(array: Array<String>, title: String, callback: (Int) -> Unit) {
        val adapter: ArrayAdapter<String> = ArrayAdapter(this@CreateFlowActivity, android.R.layout.simple_list_item_1, array)

        val builder = AlertDialog.Builder(this@CreateFlowActivity)
        builder.setTitle(title)
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
        builder.setAdapter(adapter) { dialog, which ->
            callback(which)
        }
        builder.show()
    }

    fun deleteTask(position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this@CreateFlowActivity)
        alertDialogBuilder.setTitle("Task")
                .setMessage("What would you like to do?")
                .setCancelable(true)
                .setPositiveButton("Delete") { _, _ ->
                    tasks.remove(tasks[position])
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                .setNegativeButton("Cancel") { _, _ -> }

        alertDialogBuilder.show()
    }

    fun deleteTrigger(trigger: AlarmTrigger) {
        triggersAdapter.triggers.remove(trigger)
        triggersAdapter.notifyChanged()
    }

    fun editTrigger(trigger: AlarmTrigger) {
        val listener: TimePickerDialog.OnTimeSetListener = object: TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(picker: TimePicker?, hour: Int, minute: Int) {
                val cal = Calendar.getInstance()

                val year: Int = cal.get(Calendar.YEAR)
                val month: Int = cal.get(Calendar.MONTH)
                val dayOfMonth: Int = cal.get(Calendar.DAY_OF_MONTH)

                cal.set(year, month, dayOfMonth, hour, minute)

                val timeFormatter: DateFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
                trigger.timeOfDay = timeFormatter.format(cal.time)
                triggersAdapter.notifyChanged()
            }
        }

        val cal = Calendar.getInstance()
        TimePickerDialog(this, listener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }
}
