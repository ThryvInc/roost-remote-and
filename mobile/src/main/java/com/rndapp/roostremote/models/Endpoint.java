package com.rndapp.roostremote.models;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rndapp.roostremote.Constants;

import org.json.JSONObject;

/**
 * Created by ell on 4/29/15.
 */
public class Endpoint {
    protected String name;
    protected String method;
    protected String endpoint;
    protected OptionsHolder options;

    public void execute(RequestQueue queue, ServerDescription description, Option option){
        String url = Constants.DESCRIBER_SCHEME + description.getHost() + "/" + description.getHostNamespace() + endpoint;
        try {
            JSONObject object = option.getJsonObject(options.getKey());
            JsonObjectRequest request = new JsonObjectRequest(methodStringToInt(),
                    url,
                    object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
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
