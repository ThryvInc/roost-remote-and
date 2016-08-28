package com.rndapp.roostremote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ell on 8/27/16.
 */
public class ServerDescription {
    private String host;
    @SerializedName("host_namespace")
    private String hostNamespace;
    private List<Endpoint> endpoints;

    public String getHost() {
        return host;
    }

    public String getHostNamespace() {
        return hostNamespace;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }
}
