package com.allego.scorm;

/**
 * Created by e_walker on 06.06.17.
 * cannot be implemented without creation of SCORM RTE classes
 */
public interface IStudentLog {
    public static final int FIRST_ITEM = 1;
    public static final int FIRST_CALLABLE_ITEM = 2;
    public static final int FIRST_CALLABLE_NOT_STARTED_ITEM = 3;

    public boolean isCompleted(IPackageItem item);

    public IPackageItem getSuspendedItem();

    public int getPreferedMode();


}
