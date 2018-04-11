package com.grzesica.przemek.artistlist.Container;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by przemek on 06.04.18.
 */

public class JsonObjectExtended implements IJsonObjectExtended {

    @Override
    public JSONObject setJsonObject(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
