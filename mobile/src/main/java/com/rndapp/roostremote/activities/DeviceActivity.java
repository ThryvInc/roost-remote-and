package com.rndapp.roostremote.activities;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rndapp.roostremote.Constants;
import com.rndapp.roostremote.adapters.EndpointAdapter;
import com.rndapp.roostremote.R;
import com.rndapp.roostremote.models.Device;
import com.rndapp.roostremote.models.Endpoint;
import com.rndapp.roostremote.models.Option;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 4/29/15.
 */
public class DeviceActivity extends AppCompatActivity {
    public static final String DEVICE_ID_KEY = "DEVICE_ID_KEY";
    public static final String SSIDS_KEY = "SSIDS_KEY";
    private Device device;
    protected List<Endpoint> endpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_list);
        setupWindowAnimations();

        ParseQuery<Device> query = new ParseQuery<>(Device.class);
        query.getInBackground(getIntent().getExtras().getString(DEVICE_ID_KEY), new GetCallback<Device>() {
            @Override
            public void done(Device object, ParseException e) {
                device = object;

                CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                collapsingToolbar.setTitle(device.getName());

                if (device.getImage() != null){
                    ImageView placeImageView = (ImageView) findViewById(R.id.iv_device);
                    Glide.with(DeviceActivity.this).load(device.getImage().getUrl()).centerCrop().into(placeImageView);
                }

                checkForSsids();
            }
        });

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_endpoints);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DeviceActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton button = (FloatingActionButton)findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchEndpoints();
            }
        });
    }

    private void checkForSsids(){
        List<String> ssids = getIntent().getExtras().getStringArrayList(SSIDS_KEY);
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        if (ssid.startsWith("\"")) ssid = ssid.substring(1);
        if (ssid.endsWith("\"")) ssid = ssid.substring(0, ssid.length() - 1);
        if (ssids.contains(ssid)){
            fetchEndpoints();
        }
    }

    private void setupWindowAnimations() {
        Fade fade = (Fade) TransitionInflater.from(this).inflateTransition(R.transition.fade);
        getWindow().setEnterTransition(fade);
    }

    protected void refreshUi(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_endpoints);
        recyclerView.setAdapter(new EndpointAdapter(this, device, endpoints));
    }

    public void fetchEndpoints(){
        JsonArrayRequest request = new JsonArrayRequest(
                Constants.SCHEME + device.getHost() + "/" + device.getNamespace() + "/index",
                new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Gson gson = new GsonBuilder().registerTypeAdapter(Option.class, new Option.OptionDeserializer()).create();
                Type listType = new TypeToken<ArrayList<Endpoint>>() {}.getType();
                endpoints = gson.fromJson(jsonArray.toString(), listType);
                refreshUi();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("error", "error");
            }
        });
        Volley.newRequestQueue(this).add(request);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_remote, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            fetchEndpoints();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
