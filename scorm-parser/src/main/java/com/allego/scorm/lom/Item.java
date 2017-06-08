package com.allego.scorm.lom;

import com.allego.scorm.IPackageItem;
import com.allego.scorm.IStudentLog;
import com.allego.scorm.ItemNotFounfException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by e_walker on 19.05.17.
 */
public class Item implements IPackageItem {
    private JSONObject rawData = null;
    private ArrayList<Item> items = new ArrayList();
    private String baseURL;
    private String organisationId;

    public Item(String organisationId, JSONObject data, String baseURL) {

        rawData = data;
        this.baseURL = baseURL;
        this.organisationId = organisationId;
        items = parseChilds();
    }

    public Item () {
        rawData =  new  JSONObject ();
    }

    public String getOrganisationId() {
        return organisationId;

    }

    private ArrayList<Item> parseChilds() {
        return SCORMHelper.parseItems(rawData, organisationId, baseURL);
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


    private String getRelativeURL() {
        String url = null;
        try {
            url = rawData.getString("url");
        } catch (JSONException e) {

        }
        return url;
    }

    public boolean isSCO() {
        return getRelativeURL() != null;
    }

    public String getURL() {

        String url = getRelativeURL();

        if (url != null) {
            return baseURL + url;
        } else {
            return null;
        }

    }

    public String getTitle() {
        String result = null;
        try {
            result = rawData.getString("title");
        } catch (JSONException e) {

        }
        return result;
    }

    public String getItemId() throws ItemNotFounfException {
        String result = null;
        try {
            result = rawData.getString("id");
        } catch (JSONException e) {
            throw new ItemNotFounfException(e.getMessage());
        }
        return result;
    }

    public String getRTESettings(String varName) {
        String result = "";
        if (varName != null) {
            if (varName.equals("cmi.max_time_allowed")) {
                result = SCORMHelper.getJSONVarStringDefault(rawData, "maxTimeAllowed");
            } else if (varName.equals("cmi.time_limit_action")) {
                result = SCORMHelper.getJSONVarStringDefault(rawData, "actionOnTimeLimit");
            } else if (varName.equals("cmi.launch_data")) {
                result = SCORMHelper.getJSONVarStringDefault(rawData, "dataFromLMS");
            } else if (varName.equals("cmi.student_data.mastery_score")) {
                result = SCORMHelper.getJSONVarStringDefault(rawData, "masteryScore");
            } else if (varName.equals("cmi.completion_threshold")) {
                JSONObject object = SCORMHelper.getJSONVarObjectDefault(rawData, "completionThreshold");
                String completedByMeasure = SCORMHelper.getJSONVarStringDefault(object, "completedByMeasure");
                if ((completedByMeasure != null) && (completedByMeasure.equals("true"))) {
                    result = SCORMHelper.getJSONVarStringDefault(object, "minProgressMeasure");
                } else {
                    result = SCORMHelper.getJSONVarStringDefault(object, "value");
                }
            }
        }
        return result;
    }


    public List<IPackageItem> getChilds() {
        ArrayList<IPackageItem> result = new ArrayList<IPackageItem>();
        result.addAll(items);

        return result;
    }

    // cannot be implemented without access log student data
    public boolean isEnabledForStudent(IStudentLog log) {
        String visibleForBrowser = null;
        try {
            visibleForBrowser = rawData.getString("isVisible");
        } catch (JSONException e) {

        }

        if ((visibleForBrowser == null) || (visibleForBrowser.equals("true")) || (visibleForBrowser.equals("1"))) {

            //TODO: log check goes here
            return true;
        } else {
            return false;
        }

    }
}
