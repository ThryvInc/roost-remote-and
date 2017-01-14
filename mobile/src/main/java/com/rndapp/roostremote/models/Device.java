package com.rndapp.roostremote.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by ell on 1/11/16.
 */
public class Device extends ServerObject implements Serializable{
    private String name;
    private String host;
    private String hostNamespace;
    private String describer;
    private String describerNamespace;
    private HashMap properties;
    private String imageUrl;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost(){
        return host;
    }

    public String getHostNamespace(){
        return hostNamespace;
    }

    public String getDescriber(){
        return describer;
    }

    public void setDescriber(String describer) {
        this.describer = describer;
    }

    public String getDescriberNamespace(){
        return describerNamespace;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public HashMap getProperties(){
        return properties;
    }

    public void setProperties(HashMap properties) {
        this.properties = properties;
    }
}
