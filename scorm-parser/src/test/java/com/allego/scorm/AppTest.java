package com.allego.scorm;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class AppTest {

    private String baseDir = System.getProperty("user.dir") + "/com/allego/scorm/";
    private String s3AccessKey;
    private String s3SecretKey;
    private String bucket;

    private String pathTest1 = "test1";
    private String pathTest2 = "test2";
    private String pathTest3 = "test3";


    private void deleteObjects(AmazonS3 s3client, String key) {


        boolean needClear = true;
        try {
            S3Object folder = s3client.getObject(bucket, key);


        } catch (AmazonServiceException e) {
            String errorCode = e.getErrorCode();
            if (!errorCode.equals("NoSuchKey")) {
                throw e;
            }
            needClear = false;

        }


        if (needClear) {
            ObjectListing objects;
            objects = s3client.listObjects(bucket, key);

            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                s3client.deleteObject(bucket, objectSummary.getKey());
            }
            s3client.deleteObject(bucket, key);
        }

    }


    @Before
    public void InitSuite() throws IOException {
        Properties prop = new Properties();

        FileInputStream input = new FileInputStream(AppTest.class.getResource("config.properties").getFile());

        prop.load(input);

        bucket = prop.getProperty("bucket");
        s3AccessKey = prop.getProperty("s3AccessKey");
        s3SecretKey = prop.getProperty("s3SecretKey");

        //empty test folders
       /* AWSCredentials credentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        AmazonS3 s3client = new AmazonS3Client(credentials);
        deleteObjects(s3client,pathTest1);
        deleteObjects(s3client,pathTest2);
        deleteObjects(s3client,pathTest3);*/


    }


    @After
    public void TearDown() {

    }

    @org.junit.Test
    public void testApp1() {

        IScormParser parser = ScormParserFactory.getScormParser();
        IScormParserResult resultData = null;
        boolean result = true;

        try {

            String path = AppTest.class.getResource("scorm12/golf12.zip").getFile();

            resultData = parser.parse(path, s3AccessKey, s3SecretKey, bucket, pathTest1);
        } catch (BadSCORMPackageException e) {
            e.printStackTrace();
            result = false;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result);

        Assert.assertTrue(result);
        Assert.assertNotNull(resultData);
        Assert.assertEquals(resultData.getScormVersion(), IScormParserResult.SCORM_1_2);
        Assert.assertEquals(resultData.getStatus(), IScormParserResult.STATUS_SUCCESS);
    }

    @org.junit.Test
    public void testApp2() {

        IScormParser parser = ScormParserFactory.getScormParser();
        boolean result = true;
        IScormParserResult resultData = null;

        try {

            String path = AppTest.class.getResource("scorm2004/golf2004.zip").getFile();

            resultData = parser.parse(path, s3AccessKey, s3SecretKey, bucket, pathTest2);
        } catch (BadSCORMPackageException e) {
            e.printStackTrace();
            result = false;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result);
        Assert.assertNotNull(resultData);
        Assert.assertEquals(resultData.getScormVersion(), IScormParserResult.SCORM_2004);
        Assert.assertEquals(resultData.getStatus(), IScormParserResult.STATUS_SUCCESS);
    }

    @org.junit.Test
    public void testApp3() {

        IScormParser parser = ScormParserFactory.getScormParser();
        boolean result = true;
        IScormParserResult resultData = null;

        try {

            String path = AppTest.class.getResource("scormbad/scorm.zip").getFile();

            resultData = parser.parse(path, s3AccessKey, s3SecretKey, bucket, pathTest3);
        } catch (BadSCORMPackageException e) {
            e.printStackTrace();
            result = false;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            result = false;
        }
        Assert.assertTrue(result);
        Assert.assertNotNull(resultData);
        Assert.assertEquals(resultData.getScormVersion(), IScormParserResult.INVALID_SCORM_VERSION);
        Assert.assertEquals(resultData.getStatus(), IScormParserResult.STATUS_INVALID_SCORM_FORMAT);
    }


}
