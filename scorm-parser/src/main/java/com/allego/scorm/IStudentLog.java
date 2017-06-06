package com.allego.scorm;

/**
 * Created by e_walker on 06.06.17.
 * cannot be implemented without creation of SCORM RTE classes
 */
public interface IStudentLog {
    public boolean isCompleted(IPackageItem item);

    public IPackageItem getSuspendedItem();


}
