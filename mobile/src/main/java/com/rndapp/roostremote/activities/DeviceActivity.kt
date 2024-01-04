package com.rndapp.roostremote.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import android.transition.Fade
import android.transition.TransitionInflater
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.rndapp.roostremote.Constants
import com.rndapp.roostremote.R
import com.rndapp.roostremote.adapters.EndpointAdapter
import com.rndapp.roostremote.api_calls.GetDeviceCall
import com.rndapp.roostremote.api_calls.GetDeviceTypeCall
import com.rndapp.roostremote.api_calls.VolleyManager
import com.rndapp.roostremote.models.Device
import com.rndapp.roostremote.models.Endpoint
import com.rndapp.roostremote.models.Option
import com.rndapp.roostremote.models.ServerDescription

import org.json.JSONArray
import org.json.JSONObject

import java.lang.reflect.Type
import java.nio.charset.Charset
import java.util.ArrayList

/**
 * Created by ell on 4/29/15.
 */
class DeviceActivity : AppCompatActivity() {
    private var device: Device? = null
    private var description: ServerDescription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options_list)
        setupWindowAnimations()

        device = intent.extras!!.getSerializable(DEVICE_KEY) as Device

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        1)
        }
    }

    override fun onResume() {
        super.onResume()

        GetDeviceCall.addRequestToQueue(this, device!!, Volley.newRequestQueue(this), { device ->
            this@DeviceActivity.device = device
            setup()
        }) { error ->
            error.printStackTrace()
            Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setup() {
        val collapsingToolbar = findViewById<View>(R.id.collapsing_toolbar) as CollapsingToolbarLayout
        collapsingToolbar.title = device!!.name

        val toolbar = findViewById<View>(R.id.anim_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (device?.imageUrl != null) {
            val placeImageView = findViewById<View>(R.id.iv_device) as ImageView
            Glide.with(this@DeviceActivity).load(device!!.imageUrl).centerCrop().into(placeImageView)
        }

        checkForSsids()

        val recyclerView = findViewById<View>(R.id.rv_endpoints) as androidx.recyclerview.widget.RecyclerView
        recyclerView.setHasFixedSize(true)
        val layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this@DeviceActivity)
        recyclerView.layoutManager = layoutManager

        val button = findViewById<View>(R.id.fab) as FloatingActionButton
        button.setOnClickListener { fetchEndpoints() }
    }

    private fun checkForSsids() {
        val ssids = intent.extras?.getStringArrayList(SSIDS_KEY)
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        var ssid = wifiInfo.ssid
        if (ssid.startsWith("\"")) ssid = ssid.substring(1)
        if (ssid.endsWith("\"")) ssid = ssid.substring(0, ssid.length - 1)
        if (ssids != null && ssids.contains(ssid)) {
            fetchEndpoints()
        }
    }

    private fun setupWindowAnimations() {
        val fade = TransitionInflater.from(this).inflateTransition(R.transition.fade) as Fade
        window.enterTransition = fade
    }

    protected fun refreshUi() {
        val recyclerView = findViewById<View>(R.id.rv_endpoints) as androidx.recyclerview.widget.RecyclerView
        val device = device
        val description = description
        if (device != null && description != null) {
            recyclerView.adapter = EndpointAdapter(device, description) { endpoint, option ->
                if (option != null) {
                    endpoint?.execute(device, description,
                            option, null, Response.ErrorListener { volleyError ->
                        volleyError.printStackTrace()
                        Toast.makeText(this@DeviceActivity, volleyError.localizedMessage, Toast.LENGTH_LONG).show()
                    })
                }
            }
        }
    }

    fun fetchEndpoints() {
        val deviceTypeId = device?.deviceTypeId
        if (deviceTypeId != null) {
            GetDeviceTypeCall.addRequestToQueue(this, deviceTypeId, Volley.newRequestQueue(this), { serverDescription ->
                description = serverDescription
                refreshUi()
            }, Response.ErrorListener { volleyError ->
                volleyError.printStackTrace()
                Log.e("error", volleyError.localizedMessage ?: "")
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_SHORT).show()
            })
        } else {
            val request = JsonObjectRequest(
                    Constants.DESCRIBER_SCHEME + device!!.describer + "/" + device!!.describerNamespace + "/index", null,
                    Response.Listener { jsonObject ->
                        val gson = GsonBuilder().registerTypeAdapter(Option::class.java, Option.OptionDeserializer()).create()
                        description = gson.fromJson(jsonObject.toString(), ServerDescription::class.java)
                        refreshUi()
                    }, Response.ErrorListener { volleyError ->
                volleyError.printStackTrace()
                Log.e("error", volleyError.localizedMessage ?: "")
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_SHORT).show()
            })
            request.setShouldCache(false)
            Volley.newRequestQueue(this).add(request)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_remote, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_edit) {
            val intent = Intent(this, EditDeviceActivity::class.java)
            intent.putExtra(EditDeviceActivity.DEVICE_KEY, device)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        val DEVICE_KEY = "DEVICE_ID_KEY"
        val SSIDS_KEY = "SSIDS_KEY"
    }
}
