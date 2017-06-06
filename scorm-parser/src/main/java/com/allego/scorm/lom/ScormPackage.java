package com.allego.scorm.lom;

import com.allego.scorm.IPackageItem;
import com.allego.scorm.IPackageNavigation;
import com.allego.scorm.IStudentLog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e_walker on 23.05.17.
 */
public class ScormPackage implements IPackageNavigation {

    private String packageScormVersion;

    private JSONObject rawData;
    private String location;

    private  ArrayList<Item> items = new ArrayList();
    private String defaultOrganisation;
    private String baseURL;

    public ScormPackage(String scormVersion, JSONObject data, String url) {
        packageScormVersion = scormVersion;
        rawData = data;
        items = parseChilds();
        baseURL = url;


    }

    private ArrayList<Item> parseChilds() {
        ArrayList<Item> result = new ArrayList<Item>();
        try {
            JSONArray organisations = rawData.getJSONArray("bundles");
            defaultOrganisation = rawData.getString("default");
            for (Object orgIteration : organisations) {
                JSONObject organisation = (JSONObject) orgIteration;
                String id = organisation.getString("id");
                result = SCORMHelper.parseItems(organisation, id, baseURL);
            }


        } catch (JSONException e) {

        }
        return result;

    }

    public void setLocation(String path) {
        location = path;
    }
    public String getCallPath(String item) {
        return null;
    }
    public Item getFirstCallItem() {
        if (!items.isEmpty()) {
            for (Item item : items) {
                if (item.getOrganisationId().equals(defaultOrganisation)) {
                    return item;
                }
            }

        }
        return null;

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

    public List<IPackageItem> getCoursePackage(IStudentLog student) {
        return getCoursePackage(student, defaultOrganisation);

    }

    public List<IPackageItem> getCoursePackage(IStudentLog student, String organisationID) {
        List<IPackageItem> results = new ArrayList<IPackageItem>();
        for (Item item : items) {
            if ((item.getOrganisationId().equals(organisationID)) && (item.isEnabledForStudent(student))) {
                results.add(item);
            }

        }
        return results;
    }

    public IPackageItem getMainPackage(IStudentLog student) {
        IPackageItem suspend = student.getSuspendedItem();
        if (suspend != null) {
            return suspend;
        } else {
            return getFirstCallItem();
        }

    }
}
