package com.allego.scorm;

import com.allego.scorm.lom.ScormPackage;
import com.allego.scorm.util.UnZip;

import java.io.IOException;

/**
 * Created by e_walker on 19.05.17.
 */
public class S3ScormUploader  implements IScormParser{

    private ScormPackage parseManifest(String manifest) {
        return null;
    }

    public IScormParserResult parse(String pathToInputScormPackage, String s3AccessKey, String s3SecretKey, String bucket, String parentTargetPath) throws BadSCORMPackageException {

        UnZip zip = new UnZip(pathToInputScormPackage);
        byte[] data = null;
        try {
            data = zip.getFileContent("imsmanifest.xml");
        } catch (IOException e) {

        }
        if (data == null) {
            throw new BadSCORMPackageException("The SCORM package is invalid");
        }
        String manifest = new String(data);
        System.out.println(manifest);


        S3CloudResult result = new S3CloudResult(IScormParserResult.STATUS_SUCCESS, null);
           return result;

    }
}
