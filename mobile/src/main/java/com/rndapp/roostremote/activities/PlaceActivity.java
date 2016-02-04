package com.rndapp.roostremote.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.rndapp.roostremote.R;
import com.rndapp.roostremote.adapters.DeviceAdapter;
import com.rndapp.roostremote.interfaces.OnDeviceClickedListener;
import com.rndapp.roostremote.models.Device;
import com.rndapp.roostremote.models.Place;

import java.util.List;

/**
 * Created by ell on 1/10/16.
 */
public class PlaceActivity extends AppCompatActivity {
    private Place place;
    private List<Device> devices;
    private RecyclerView recyclerView;
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        setupWindowAnimations();

        place = ((List<Place>)ParseUser.getCurrentUser().get("places")).get(0);
        devices = place.getDevices();
        adapter = new DeviceAdapter(devices, new OnDeviceClickedListener() {
            @Override
            public void onDeviceClicked(Device device) {
                Intent intent = new Intent(PlaceActivity.this, DeviceActivity.class);
                intent.putExtra(DeviceActivity.DEVICE_ID_KEY, device.getObjectId());
                intent.putStringArrayListExtra(DeviceActivity.SSIDS_KEY, place.getSsids());
                startActivity(intent);
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_place_name);
        title.setText(place.getName());

        ImageView placeImageView = (ImageView) findViewById(R.id.iv_place_bg);
        Glide.with(this).load(place.getImage().getUrl()).centerCrop().into(placeImageView);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.rv_devices);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void setupWindowAnimations() {
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.fade);
        getWindow().setEnterTransition(fade);
    }
}
