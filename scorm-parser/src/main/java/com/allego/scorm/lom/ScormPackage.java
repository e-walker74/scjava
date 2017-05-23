package com.allego.scorm.lom;

import com.allego.scorm.IScormParserResult;

import java.util.ArrayList;

/**
 * Created by e_walker on 23.05.17.
 */
public class ScormPackage {

    private String packageScormVersion;

    private  ArrayList<Item> items = new ArrayList();

    public ScormPackage(String scormVersion ) {
        packageScormVersion = scormVersion;

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
