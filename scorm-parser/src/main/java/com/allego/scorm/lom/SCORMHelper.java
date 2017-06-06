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

}
