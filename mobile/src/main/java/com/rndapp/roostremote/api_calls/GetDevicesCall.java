package com.rndapp.roostremote.api_calls;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rndapp.roostremote.Constants;
import com.rndapp.roostremote.models.Device;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ell on 8/22/16.
 */
public class GetDevicesCall {

    public interface DevicesListener {
        void onDevicesParsed(List<Device> devices);
    }

    public static void addRequestToQueue(final Context context, String placeId, RequestQueue queue,
                                         final DevicesListener listener, Response.ErrorListener errorListener){
        String url = Constants.USER_URL + "places/" + placeId + "/devices";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response", response.toString());
                List<Device> devices = new Gson().fromJson(response.toString(), new TypeToken<ArrayList<Device>>(){}.getType());
                listener.onDevicesParsed(devices);
            }
        }, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return AuthenticatedCall.getHeaders(context);
            }
        };
        queue.add(request);
    }
}
