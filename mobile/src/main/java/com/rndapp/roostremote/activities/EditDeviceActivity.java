package com.rndapp.roostremote.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rndapp.roostremote.R;
import com.rndapp.roostremote.api_calls.EditDeviceCall;
import com.rndapp.roostremote.models.Device;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by ell on 8/26/16.
 */
public class EditDeviceActivity extends AppCompatActivity {
    public static final String DEVICE_KEY = "DEVICE_ID_KEY";
    private Device device;
    private EditText nameEditText;
    private EditText describerEditText;
    private EditText stateEditText;
    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        device = (Device)getIntent().getExtras().getSerializable(DEVICE_KEY);

        nameEditText = (EditText) findViewById(R.id.et_name);
        describerEditText = (EditText) findViewById(R.id.et_describer);
        stateEditText = (EditText) findViewById(R.id.et_state);

        nameEditText.setText(device.getName());
        describerEditText.setText(device.getDescriber());
        stateEditText.setText(new JSONObject(device.getProperties()).toString());

        queue = Volley.newRequestQueue(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_save) {
            try {
                JsonFactory factory = new JsonFactory();
                JsonParser parser  = factory.createParser(stateEditText.getText().toString());
                HashMap<String, Object> state = new ObjectMapper().readValue(parser, HashMap.class);
                device.setProperties(state);
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(this, "State invalid", Toast.LENGTH_SHORT).show();
            }
            device.setName(nameEditText.getText().toString());
            device.setDescriber(describerEditText.getText().toString());
            EditDeviceCall.addRequestToQueue(EditDeviceActivity.this, device, queue, new EditDeviceCall.CallFinishedListener() {
                @Override
                public void callFinishedSuccessfully() {
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(EditDeviceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
