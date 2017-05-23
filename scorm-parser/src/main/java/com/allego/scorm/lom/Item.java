package com.allego.scorm.lom;

import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by e_walker on 19.05.17.
 */
public class Item {
    private JSONObject rawData = null;
    private ArrayList<Item> items = new ArrayList();

    public Item (JSONObject data) {
        rawData = data;
    }

    public Item () {
        rawData =  new  JSONObject ();
    }
    private ArrayList extractChilds() {
        ArrayList<Item> result = new ArrayList();
        return result;
    }

    public String  getItemName() {
        return null;
    }
    public void setProperty (String propName, String value) {
        rawData.accumulate(propName, value);

    }
    public void AddChild(Item item) {
        items.add(item);
        rawData.append("childs",item.toJSON());
    }


    public String getEntryPoint() {
        return rawData.getString("url");
    }
    public ArrayList<Item> getItems() {
        return items;
    }

    public JSONObject toJSON () {
        return rawData;
    }
    public boolean hasChilds() {
        return !items.isEmpty();
    }


}
