package com.allego.scorm;

/**
 * Created by e_walker on 19.05.17.
 */
public class S3ScormUploader  implements IScormParser{
    public IScormParserResult parse(String pathToInputScormPackage, String s3AccessKey, String s3SecretKey, String bucket, String parentTargetPath) {
           S3CloudResult result = new  S3CloudResult(IScormParserResult.STATUS_SUCCESS, null);
           return result;

    }
}
