package com.rndapp.roostremote.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.rndapp.roostremote.R;
import com.rndapp.roostremote.api_calls.EditDeviceCall;
import com.rndapp.roostremote.models.Device;

/**
 * Created by ell on 8/26/16.
 */
public class EditDeviceActivity extends AppCompatActivity {
    public static final String DEVICE_KEY = "DEVICE_ID_KEY";
    private Device device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        device = (Device)getIntent().getExtras().getSerializable(DEVICE_KEY);

        Button saveButton = (Button) findViewById(R.id.btn_save);
        final EditText nameEditText = (EditText) findViewById(R.id.et_name);
        final EditText describerEditText = (EditText) findViewById(R.id.et_describer);

        nameEditText.setText(device.getName());
        describerEditText.setText(device.getDescriber());

        final RequestQueue queue = Volley.newRequestQueue(this);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

    }
}
