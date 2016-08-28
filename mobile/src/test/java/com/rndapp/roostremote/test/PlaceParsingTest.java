package com.rndapp.roostremote.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rndapp.roostremote.models.Place;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by ell on 8/22/16.
 */
public class PlaceParsingTest {

    @Test
    public void parsingPlaceTest(){
        String jsonString = "{\"_id\":\"57bb220c88949eea76a28819\",\"userId\":\"57ba88db07735e110020006b\",\"name\":\"The Roost\",\"__v\":2,\"ssids\":[\"Turin\",\"Turin-5G\"]}";
        Place place = new Gson().fromJson(jsonString, Place.class);
        assertThat(place.getId().equals("57bb220c88949eea76a28819"), is(true));
    }

    @Test
    public void parsingPlacesTest(){
        String jsonString = "[{\"_id\":\"57bb220c88949eea76a28819\",\"userId\":\"57ba88db07735e110020006b\",\"name\":\"The Roost\",\"__v\":2,\"devices\":[\"57bb334c88949eea76a2881a\",\"57bb3377361c3f1d04e70d9e\"],\"ssids\":[\"Turin\",\"Turin-5G\"]}]";
        List<Place> places = new Gson().fromJson(jsonString, new TypeToken<ArrayList<Place>>(){}.getType());
        assertThat(places.get(0).getId(), is("57bb220c88949eea76a28819"));
    }


}
