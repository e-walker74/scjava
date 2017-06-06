package com.allego.scorm;

/**
 * Created by e_walker on 06.06.17.
 */
public interface IPackageItem {
    String getURL();

    String getTitle();

    String getItemId() throws ItemNotFounfException;

}
