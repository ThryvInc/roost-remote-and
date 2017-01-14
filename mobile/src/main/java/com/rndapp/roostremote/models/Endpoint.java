package com.rndapp.roostremote.models;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rndapp.roostremote.Constants;
import com.rndapp.roostremote.activities.LoginActivity;

import org.json.JSONObject;

/**
 * Created by ell on 4/29/15.
 */
public class Endpoint {
    protected String name;
    protected String method;
    protected String endpoint;
    protected OptionsHolder options;

    public void execute(final RequestQueue queue, ServerDescription description, Option option, Response.ErrorListener errorListener){
        String url = Constants.DESCRIBER_SCHEME + description.getHost() + "/" + description.getHostNamespace() + endpoint;
        try {
            JSONObject object = option.getJsonObject(options.getKey());
            Option.addStaticValues(object, options.getStaticValues());
            Log.d("object", object.toString());
            JsonObjectRequest request = new JsonObjectRequest(methodStringToInt(),
                    url,
                    object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("response", jsonObject.toString());
                        }
                    }, errorListener);
            queue.add(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected int methodStringToInt(){
        if (method.equals("GET")) return Request.Method.GET;
        if (method.equals("PUT")) return Request.Method.PUT;
        if (method.equals("POST")) return Request.Method.POST;
        if (method.equals("DELETE")) return Request.Method.DELETE;
        return -1;
    }

    public String getName() {
        return name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getMethod() {
        return method;
    }

    public OptionsHolder getOptionsHolder() {
        return options;
    }
}
