package com.rndapp.roostremote.api_calls;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.rndapp.roostremote.Constants;
import com.rndapp.roostremote.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ell on 8/22/16.
 */
public class LoginCall {
    public interface UserListener {
        void onUserParsed(User user);
    }

    public static void addRequestToQueue(final Context context, String username, String password, RequestQueue queue,
                                         final UserListener listener, Response.ErrorListener errorListener){
        String url = Constants.USER_URL + "session";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                User user = new Gson().fromJson(response.toString(), User.class);
                Constants.setApiKey(context, user.getApiKey());
                listener.onUserParsed(user);
            }
        }, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_HEADER);
                return headers;
            }
        };
        queue.add(request);
    }
}
