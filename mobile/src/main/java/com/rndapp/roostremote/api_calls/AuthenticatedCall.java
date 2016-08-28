package com.rndapp.roostremote.api_calls;

import android.content.Context;

import com.rndapp.roostremote.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ell on 8/22/16.
 */
public class AuthenticatedCall {
    public static Map<String, String> getHeaders(Context context){
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_HEADER);
        headers.put(Constants.API_KEY_HEADER, Constants.getApiKey(context));
        return headers;
    }
}
