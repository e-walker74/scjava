package com.allego.scorm;

import java.util.List;

/**
 * Created by e_walker on 06.06.17.
 */
public interface IPackageNavigation {
    List<IPackageItem> getCoursePackage(IStudentLog student);

    List<IPackageItem> getCoursePackage(IStudentLog student, String organisationID);

    IPackageItem getMainPackage(IStudentLog student);
}
