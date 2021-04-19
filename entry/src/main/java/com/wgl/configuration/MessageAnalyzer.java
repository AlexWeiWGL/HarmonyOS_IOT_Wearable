package com.wgl.configuration;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;

public class MessageAnalyzer {
    public static HashMap<String, Integer> getData(JSONObject jsonObject){
        HashMap<String, Integer> items = new HashMap<>();

        JSONArray shadow = jsonObject.getJSONArray("shadow");
        JSONObject reported = shadow.getJSONObject(0).getJSONObject("reported");
        JSONObject properties = reported.getJSONObject("properties");

        int temperature = properties.getInt("Temperature");
        int humidity = properties.getInt("Humidity");
        int luminance = properties.getInt("Luminance");

        items.put("Temperature", temperature);
        items.put("Humidity", humidity);
        items.put("Luminance", luminance);

        System.out.println(temperature);

        return items;
    }
}
