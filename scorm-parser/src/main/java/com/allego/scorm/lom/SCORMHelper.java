package com.allego.scorm.lom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by e_walker on 06.06.17.
 */
public class SCORMHelper {
    public static ArrayList<Item> parseItems(JSONObject data, String organisationId, String baseURL) {
        ArrayList<Item> result = new ArrayList<Item>();
        try {
            JSONArray rawItems = data.getJSONArray("items");
            for (Object itemIteration : rawItems) {
                JSONObject rawItem = (JSONObject) itemIteration;

                result.add(new Item(organisationId, rawItem, baseURL));
            }

        } catch (JSONException e) {

        }
        return result;
    }

    public static String getJSONVarStringDefault(JSONObject object, String varName) {
        return getJSONVarStringDefault(object, varName, null);
    }

    public static String getJSONVarStringDefault(JSONObject object, String varName, String defaultVal) {
        String result = defaultVal;
        try {
            result = object.getString(varName);


        } catch (JSONException e) {

        }
        return result;
    }

    public static JSONObject getJSONVarObjectDefault(JSONObject object, String varName) {
        JSONObject result = null;
        try {
            result = object.getJSONObject(varName);


        } catch (JSONException e) {

        }
        return result;

    }

}
