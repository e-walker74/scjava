package com.allego.scorm;

import com.allego.scorm.lom.*;

/**
 * Created by e_walker on 23.05.17.
 */
public class S3CloudResult  implements  IScormParserResult{

    private ScormPackage uploadedPackage = null;
    private int status;

    public S3CloudResult (int status,  ScormPackage scormPackage) {
        this.status = status;
        uploadedPackage = scormPackage;

    }

    public String getScormVersion() throws PackageNotFoundException {
        if (uploadedPackage == null){
            throw new PackageNotFoundException("Package is not created");
        }
        return uploadedPackage.getScormVersion();
    }

    public int getStatus() {
        return status;
    }

    public String getScormContentEntryPoint() throws PackageNotFoundException {

        if (uploadedPackage == null){
            throw new PackageNotFoundException("Package is not created");
        }
        return uploadedPackage.getFirstCallItem().getEntryPoint();
    }
}
