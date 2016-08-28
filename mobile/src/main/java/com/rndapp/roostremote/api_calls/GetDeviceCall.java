package com.rndapp.roostremote.api_calls;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.rndapp.roostremote.Constants;
import com.rndapp.roostremote.models.Device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ell on 8/26/16.
 */
public class GetDeviceCall extends AuthenticatedCall {

    public interface DeviceListener {
        void onDeviceParsed(Device device);
    }

    public static void addRequestToQueue(final Context context, Device device, RequestQueue queue,
                                         final DeviceListener listener, Response.ErrorListener errorListener){
        String url = Constants.USER_URL + "devices/" + device.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", device.getName());
            jsonObject.put("describer", device.getDescriber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Device updatedDevice = new Gson().fromJson(response.toString(), Device.class);
                listener.onDeviceParsed(updatedDevice);
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
