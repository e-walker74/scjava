package com.allego.scorm;

/**
 * Created by e_walker on 17.05.17.
 */
public interface IScormParserResult {

    public static final int	STATUS_SUCCESS = 0;
    public static final int	STATUS_AWS_S3_EXCEPTION = -1;
    public static final int	STATUS_INVALID_SCORM_FORMAT = -2;
    public static final int	STATUS_UNSUPPORTED_SCORM_VERSION = -4;

    public String	getScormVersion() throws PackageNotFoundException;
    public int	    getStatus();
    public String   getScormContentEntryPoint() throws PackageNotFoundException;

}
