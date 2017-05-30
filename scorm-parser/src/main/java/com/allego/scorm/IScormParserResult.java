package com.allego.scorm;

/**
 * Created by e_walker on 17.05.17.
 */
public interface IScormParserResult {

    public static final int	STATUS_SUCCESS = 0;
    public static final int	STATUS_AWS_S3_EXCEPTION = -1;
    public static final int	STATUS_INVALID_SCORM_FORMAT = -2;
    public static final int	STATUS_UNSUPPORTED_SCORM_VERSION = -4;

    public static final String SCORM_1_2 = "SCORM 1.2";
    public static final String SCORM_2004 = "SCORM 2004";
    public static final String INVALID_SCORM_VERSION = "INVALID SCORM FORMAT";

    public String getScormVersion();
    public int	    getStatus();
    public String   getScormContentEntryPoint() throws PackageNotFoundException;

}
