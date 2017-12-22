package com.rndapp.roostremote.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast

import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.rndapp.roostremote.R
import com.rndapp.roostremote.api_calls.EditDeviceCall
import com.rndapp.roostremote.models.Device

import org.json.JSONObject

import java.io.IOException
import java.util.HashMap

/**
 * Created by ell on 8/26/16.
 */
class EditDeviceActivity : AppCompatActivity() {
    private var device: Device? = null
    private var nameEditText: EditText? = null
    private var describerEditText: EditText? = null
    private var stateEditText: EditText? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        device = intent.extras!!.getSerializable(DEVICE_KEY) as Device

        nameEditText = findViewById(R.id.et_name)
        describerEditText = findViewById(R.id.et_describer)
        stateEditText = findViewById(R.id.et_state)

        nameEditText!!.setText(device!!.name)
        describerEditText!!.setText(device!!.describer)
        if (device?.properties != null) {
            stateEditText!!.setText(JSONObject(device?.properties).toString())
        } else {
            stateEditText?.visibility = View.GONE
        }

        queue = Volley.newRequestQueue(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_save) {
            try {
                val factory = JsonFactory()
                val parser = factory.createParser(stateEditText!!.text.toString())
                val state = ObjectMapper().readValue<HashMap<*, *>>(parser, HashMap::class.java!!)
                device!!.properties = state
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "State invalid", Toast.LENGTH_SHORT).show()
            }

            device!!.name = nameEditText!!.text.toString()
            device!!.describer = describerEditText!!.text.toString()
            EditDeviceCall.addRequestToQueue(this@EditDeviceActivity, device, queue, { finish() }) { error ->
                error.printStackTrace()
                Toast.makeText(this@EditDeviceActivity, error.message, Toast.LENGTH_SHORT).show()
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        val DEVICE_KEY = "DEVICE_ID_KEY"
    }
}
