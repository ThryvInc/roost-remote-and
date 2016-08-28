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
import com.rndapp.roostremote.models.Place;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ell on 8/22/16.
 */
public class GetPlacesCall extends AuthenticatedCall{

    public interface PlacesListener {
        void onPlacesParsed(List<Place> places);
    }

    public static void addRequestToQueue(final Context context, RequestQueue queue,
                                         final PlacesListener listener, Response.ErrorListener errorListener){
        String url = Constants.USER_URL + "places";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Place> places = new Gson().fromJson(response.toString(), new TypeToken<ArrayList<Place>>(){}.getType());
                listener.onPlacesParsed(places);
            }
        }, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return AuthenticatedCall.getHeaders(context);
            }
        };
        queue.add(request);
    }
}
