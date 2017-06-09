package com.allego.scorm;

import com.allego.scorm.lom.ScormPackage;

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

    public String getScormVersion() {
        if (uploadedPackage == null){
            return IScormParserResult.INVALID_SCORM_VERSION;
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
        return uploadedPackage.getFirstItem().getEntryPoint();
    }

    public IPackageNavigation getPackageNavigation() {
        return uploadedPackage;
    }
}
