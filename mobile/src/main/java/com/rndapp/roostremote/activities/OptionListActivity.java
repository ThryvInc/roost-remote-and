package com.rndapp.roostremote.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rndapp.roostremote.Constants;
import com.rndapp.roostremote.EndpointAdapter;
import com.rndapp.roostremote.R;
import com.rndapp.roostremote.models.Endpoint;
import com.rndapp.roostremote.models.Option;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ell on 4/29/15.
 */
public class OptionListActivity extends ActionBarActivity {
    protected List<Endpoint> endpoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_list);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_endpoints);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OptionListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        FloatingActionButton button = (FloatingActionButton)findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchEndpoints();
            }
        });
    }

    protected void refreshUi(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv_endpoints);
        recyclerView.setAdapter(new EndpointAdapter(this, endpoints));
    }

    public void fetchEndpoints(){
        JsonArrayRequest request = new JsonArrayRequest(
                Constants.BASE_URL + "/index",
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
        }
                );
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
