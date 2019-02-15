package com.rndapp.roostremote.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.TransitionInflater
import android.widget.Toast

import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.rndapp.roostremote.R
import com.rndapp.roostremote.adapters.DeviceAdapter
import com.rndapp.roostremote.api_calls.GetDevicesCall
import com.rndapp.roostremote.interfaces.OnDeviceClickedListener
import com.rndapp.roostremote.models.Device
import com.rndapp.roostremote.models.Place

import kotlinx.android.synthetic.main.activity_place.*

import java.util.ArrayList

/**
 * Created by ell on 1/10/16.
 */
class PlaceActivity : AppCompatActivity() {
    private var queue: RequestQueue? = null
    private var place: Place? = null
    private var devices: List<Device>? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: DeviceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        setupWindowAnimations()

        queue = Volley.newRequestQueue(this)

        place = intent.extras!!.getSerializable(PLACE_KEY) as Place

        nameTextView.text = place!!.name

        tasksButton.setOnClickListener {
            startActivity(Intent(this@PlaceActivity, FlowsActivity::class.java))
        }

        if (place!!.imageUrl != null) {
            Glide.with(this).load(place!!.imageUrl).centerCrop().into(placeBgImageView)
        }
    }

    override fun onResume() {
        super.onResume()

        GetDevicesCall.addRequestToQueue(this, place!!.id, queue!!, { devices ->
            this@PlaceActivity.devices = devices
            adapter = DeviceAdapter(devices, object: OnDeviceClickedListener {
                override fun onDeviceClicked(device: Device) {
                    val intent = Intent(this@PlaceActivity, DeviceActivity::class.java)
                    intent.putExtra(DeviceActivity.DEVICE_KEY, device)
                    intent.putStringArrayListExtra(DeviceActivity.SSIDS_KEY, ArrayList(place!!.ssids))
                    startActivity(intent)
                }
            })

            val manager = LinearLayoutManager(this@PlaceActivity, LinearLayoutManager.VERTICAL, false)
            recyclerView = devicesRecyclerView
            recyclerView!!.layoutManager = manager
            recyclerView!!.adapter = adapter
        }) { error -> Toast.makeText(this@PlaceActivity, error.message, Toast.LENGTH_SHORT).show() }
    }

    private fun setupWindowAnimations() {
        val fade = TransitionInflater.from(this).inflateTransition(R.transition.fade) as Fade
        window.enterTransition = fade
    }

    companion object {
        val PLACE_KEY = "PLACE_KEY"
    }
}
