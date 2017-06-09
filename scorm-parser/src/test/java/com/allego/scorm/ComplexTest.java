package com.allego.scorm;

import com.allego.scorm.lom.Item;
import org.junit.Assert;
import org.junit.Before;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by e_walker on 09.06.17.
 */

public class ComplexTest {
    private String baseDir = System.getProperty("user.dir") + "/com/allego/scorm/";
    private String s3AccessKey;
    private String s3SecretKey;
    private String bucket;


    private String pathTest1 = "test1";

    private IStudentLog log;

    @Before
    public void setUp() throws Exception {
        Properties prop = new Properties();

        FileInputStream input = new FileInputStream(AppTest.class.getResource("config.properties").getFile());

        prop.load(input);

        log = mock(IStudentLog.class);
        when(log.getSuspendedItem()).thenReturn(null);

        when(log.isCompleted(any(Item.class))).thenReturn(false);
        when(log.getPreferedMode()).thenReturn(IStudentLog.FIRST_CALLABLE_ITEM);


        bucket = prop.getProperty("bucket");
        s3AccessKey = prop.getProperty("s3AccessKey");
        s3SecretKey = prop.getProperty("s3SecretKey");

    }

    @org.junit.Test
    public void testApp1() {

        IScormParser parser = ScormParserFactory.getScormParser();
        IScormParserResult resultData = null;
        boolean result = true;

        IPackageItem item = null;


        try {

            String path = AppTest.class.getResource("scorm12/The_Human_Eye.zip").getFile();

            resultData = parser.parse(path, s3AccessKey, s3SecretKey, bucket, pathTest1);

            IPackageNavigation navigation = resultData.getPackageNavigation();
            item = navigation.getMainPackage(log);


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

        try {
            Assert.assertEquals("I_A001", item.getItemId());
            Assert.assertEquals("The Human Eye", item.getTitle());
            Assert.assertEquals("test1/index.html", item.getURL());

            Assert.assertNull(item.getRTESettings("cmi.max_time_allowed"));
            Assert.assertNull(item.getRTESettings("cmi.launch_data"));
            Assert.assertEquals("75", item.getRTESettings("cmi.student_data.mastery_score"));
            Assert.assertEquals("continue,no message", item.getRTESettings("cmi.time_limit_action"));


        } catch (ItemNotFounfException e) {

            e.printStackTrace();


        }


    }

}