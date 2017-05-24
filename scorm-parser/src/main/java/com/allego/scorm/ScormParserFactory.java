package com.allego.scorm;

/**
 * Created by e_walker on 17.05.17.
 */
public class ScormParserFactory {

    public static IScormParser getScormParser() {

        return new S3ScormUploader();
    }

}
