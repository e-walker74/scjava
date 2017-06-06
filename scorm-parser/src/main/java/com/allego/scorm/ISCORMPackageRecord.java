package com.allego.scorm;

import org.json.JSONObject;

/**
 * Created by e_walker on 06.06.17.
 * for DB connectivity data required to store in DB
 * proposed DB structure
 * Id : PRIMARY_KEY
 * package_data: json string of course structure
 * url: package url compatibile with application domain
 * package
 */
public interface ISCORMPackageRecord {
    public JSONObject getPackageData();

    public String getPackageURL();

    public String getScormVersion();


}
