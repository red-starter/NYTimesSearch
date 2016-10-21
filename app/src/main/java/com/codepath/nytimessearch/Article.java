package com.codepath.nytimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by michaelsova on 10/19/16.
 */

public class Article implements Serializable {
    String webUrl;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    String headline;
    String thumbnail;

    public Article(JSONObject jsonObject){
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0){
                this.thumbnail = "http://www.nytimes.com/" + multimedia.getJSONObject(0).getString("url");
            } else {
                this.thumbnail = "";
            }

        } catch (JSONException e){

        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray jsonArray) {
        ArrayList results = new ArrayList<>();
        for (int i = 0 ; i<jsonArray.length(); i++){
            try {
                results.add(new Article(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
