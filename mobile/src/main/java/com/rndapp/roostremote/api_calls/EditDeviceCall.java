package com.rndapp.roostremote.api_calls;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rndapp.roostremote.Constants;
import com.rndapp.roostremote.models.Device;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by ell on 8/26/16.
 */
public class EditDeviceCall extends AuthenticatedCall{

    public interface CallFinishedListener {
        void callFinishedSuccessfully();
    }

    public static void addRequestToQueue(final Context context, Device device, RequestQueue queue,
                                         final CallFinishedListener listener, Response.ErrorListener errorListener){
        String url = Constants.USER_URL + "devices/" + device.getId();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", device.getName());
            jsonObject.put("describer", device.getDescriber());
            jsonObject.put("properties", new JSONObject(device.getProperties()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.callFinishedSuccessfully();
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
