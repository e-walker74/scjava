

package com.allego.scorm;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by e_walker on 17.05.17.
 */
public interface IScormParser {
    IScormParserResult  parse(String pathToInputScormPackage,
                              String s3AccessKey,
                              String s3SecretKey,
                              String bucket,
                              String parentTargetPath,
                              String baseURL) throws BadSCORMPackageException, ParserConfigurationException;



}
