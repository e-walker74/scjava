package com.allego.scorm;

import com.allego.scorm.lom.Item;
import com.allego.scorm.lom.ScormPackage;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by e_walker on 06.06.17.
 */
public class AppTest2 {
    private IStudentLog log;

    private ISCORMPackageRecord record12;
    private ISCORMPackageRecord record2004;

    @Before
    public void InitSuite() throws IOException {

        log = mock(IStudentLog.class);
        when(log.getSuspendedItem()).thenReturn(null);

        when(log.isCompleted(any(Item.class))).thenReturn(false);


        record12 = mock(ISCORMPackageRecord.class);
        InputStream file = AppTest.class.getResource("scorm12/scorm12.json").openStream();
        Scanner s = new Scanner(file).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";

        JSONObject data12 = new JSONObject(result);
        when(record12.getScormVersion()).thenReturn(IScormParserResult.SCORM_1_2);
        when(record12.getPackageURL()).thenReturn("http://test-bucket.com");
        when(record12.getPackageData()).thenReturn(data12);


        record2004 = mock(ISCORMPackageRecord.class);

        file = AppTest.class.getResource("scorm2004/scorm2004.json").openStream();
        s = new Scanner(file).useDelimiter("\\A");
        result = s.hasNext() ? s.next() : "";

        JSONObject data2004 = new JSONObject(result);
        when(record2004.getScormVersion()).thenReturn(IScormParserResult.SCORM_2004);
        when(record2004.getPackageURL()).thenReturn("http://test-bucket.com");
        when(record2004.getPackageData()).thenReturn(data2004);
    }

    @After
    public void TearDown() {

    }

    @org.junit.Test
    public void testApp1() {
        ScormPackage testPackage = new ScormPackage(record12);
        IPackageNavigation navigation = testPackage;
        IPackageItem item = navigation.getMainPackage(log);
        try {
            Assert.assertEquals(item.getItemId(), "playing_item");
            Assert.assertEquals(item.getTitle(), "Playing the Game");
            Assert.assertEquals(item.getURL(), "http://test-bucket.com");

            List<IPackageItem> items = navigation.getCoursePackage(log);

            Assert.assertEquals(items.size(), 4);
        } catch (ItemNotFounfException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testApp2() {
        ScormPackage testPackage = new ScormPackage(record2004);
        IPackageNavigation navigation = testPackage;
        IPackageItem item = navigation.getMainPackage(log);
        try {
            Assert.assertEquals(item.getItemId(), "playing_item");
            Assert.assertEquals(item.getTitle(), "Playing the Game");
            Assert.assertEquals(item.getURL(), "http://test-bucket.com");

            List<IPackageItem> items = navigation.getCoursePackage(log);

            Assert.assertEquals(items.size(), 4);
        } catch (ItemNotFounfException e) {
            e.printStackTrace();
        }
    }

}

