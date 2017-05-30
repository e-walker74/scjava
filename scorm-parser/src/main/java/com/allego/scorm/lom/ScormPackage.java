package com.allego.scorm.lom;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by e_walker on 23.05.17.
 */
public class ScormPackage {

    private String packageScormVersion;

    private JSONObject rawData;
    private String location;

    private  ArrayList<Item> items = new ArrayList();

    public ScormPackage(String scormVersion, JSONObject data) {
        packageScormVersion = scormVersion;
        rawData = data;


    }

    public void setLocation(String path) {
        location = path;
    }
    public String getCallPath(String item) {
        return null;
    }
    public Item getFirstCallItem() {
        if (items.isEmpty()) {
            return null;
        } else {
            return items.get(0);
        }

    }
    public String getScormVersion() {
        return packageScormVersion;
    }

    private String getItemsContentEntryPoint (String itemName, ArrayList<Item> scope ) {
        for (Item currItem : scope) {
            if (itemName.equals(currItem.getItemName())) {
                return currItem.getEntryPoint();
            }
            if (currItem.hasChilds()) {
                ArrayList<Item> childItems = currItem.getItems();
                String url =  getItemsContentEntryPoint(itemName,childItems);
                if (url != null) {
                    return url;
                }
            }

        }
        return null;
    }



    public String getScormContentEntryPoint(String itemName ) {
        return getItemsContentEntryPoint(itemName,items);
    }

}
