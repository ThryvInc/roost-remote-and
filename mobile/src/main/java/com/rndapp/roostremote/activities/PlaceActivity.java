package com.rndapp.roostremote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.rndapp.roostremote.R;
import com.rndapp.roostremote.adapters.DeviceAdapter;
import com.rndapp.roostremote.api_calls.GetDevicesCall;
import com.rndapp.roostremote.interfaces.OnDeviceClickedListener;
import com.rndapp.roostremote.models.Device;
import com.rndapp.roostremote.models.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 1/10/16.
 */
public class PlaceActivity extends AppCompatActivity {
    public static final String PLACE_KEY = "PLACE_KEY";
    private RequestQueue queue;
    private Place place;
    private List<Device> devices;
    private RecyclerView recyclerView;
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        setupWindowAnimations();

        queue = Volley.newRequestQueue(this);

        place = (Place)getIntent().getExtras().getSerializable(PLACE_KEY);


        TextView title = (TextView) findViewById(R.id.tv_place_name);
        title.setText(place.getName());

        if (place.getImageUrl() != null){
            ImageView placeImageView = (ImageView) findViewById(R.id.iv_place_bg);
            Glide.with(this).load(place.getImageUrl()).centerCrop().into(placeImageView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        GetDevicesCall.addRequestToQueue(this, place.getId(), queue, new GetDevicesCall.DevicesListener() {
            @Override
            public void onDevicesParsed(List<Device> devices) {
                PlaceActivity.this.devices = devices;
                adapter = new DeviceAdapter(devices, new OnDeviceClickedListener() {
                    @Override
                    public void onDeviceClicked(Device device) {
                        Intent intent = new Intent(PlaceActivity.this, DeviceActivity.class);
                        intent.putExtra(DeviceActivity.DEVICE_KEY, device);
                        intent.putStringArrayListExtra(DeviceActivity.SSIDS_KEY, new ArrayList<>(place.getSsids()));
                        startActivity(intent);
                    }
                });

                LinearLayoutManager manager = new LinearLayoutManager(PlaceActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView = (RecyclerView) findViewById(R.id.rv_devices);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlaceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupWindowAnimations() {
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.fade);
        getWindow().setEnterTransition(fade);
    }
}
