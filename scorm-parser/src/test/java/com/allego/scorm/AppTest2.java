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

    private IStudentLog log2;

    private ISCORMPackageRecord record12;
    private ISCORMPackageRecord record2004;

    @Before
    public void InitSuite() throws IOException {

        log = mock(IStudentLog.class);
        when(log.getSuspendedItem()).thenReturn(null);

        when(log.isCompleted(any(Item.class))).thenReturn(false);
        when(log.getPreferedMode()).thenReturn(IStudentLog.FIRST_ITEM);


        log2 = mock(IStudentLog.class);
        when(log2.getSuspendedItem()).thenReturn(null);

        when(log2.isCompleted(any(Item.class))).thenReturn(false);
        when(log2.getPreferedMode()).thenReturn(IStudentLog.FIRST_CALLABLE_ITEM);



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
            Assert.assertEquals("playing_item", item.getItemId());
            Assert.assertEquals("Playing the Game", item.getTitle());
            Assert.assertEquals(null, item.getURL());

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
            Assert.assertEquals("playing_item", item.getItemId());
            Assert.assertEquals("Playing the Game", item.getTitle());
            Assert.assertEquals(null, item.getURL());

            List<IPackageItem> items = navigation.getCoursePackage(log);

            Assert.assertEquals(items.size(), 4);
        } catch (ItemNotFounfException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testApp3() {
        ScormPackage testPackage = new ScormPackage(record12);
        IPackageNavigation navigation = testPackage;
        IPackageItem item = navigation.getMainPackage(log2);

        try {

            Assert.assertEquals("playing_playing_item", item.getItemId());
            Assert.assertEquals("How to Play", item.getTitle());
            Assert.assertEquals("http://test-bucket.comPlaying/Playing.html", item.getURL());

            Assert.assertEquals("00:00:30", item.getRTESettings("cmi.max_time_allowed"));
            Assert.assertEquals("test data", item.getRTESettings("cmi.launch_data"));
            Assert.assertEquals("0.75", item.getRTESettings("cmi.student_data.mastery_score"));
            Assert.assertNull(item.getRTESettings("cmi.time_limit_action"));





        } catch (ItemNotFounfException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void testApp4() {
        ScormPackage testPackage = new ScormPackage(record2004);
        IPackageNavigation navigation = testPackage;
        IPackageItem item = navigation.getMainPackage(log2);

        try {

            Assert.assertEquals("playing_playing_item", item.getItemId());
            Assert.assertEquals("How to Play", item.getTitle());
            Assert.assertEquals("http://test-bucket.comPlaying/Playing.html", item.getURL());

            Assert.assertEquals("exit,no message", item.getRTESettings("cmi.time_limit_action"));
            Assert.assertEquals("test data", item.getRTESettings("cmi.launch_data"));
            Assert.assertEquals("0.5", item.getRTESettings("cmi.completion_threshold"));
            Assert.assertNull(item.getRTESettings("cmi.max_time_allowed"));


        } catch (ItemNotFounfException e) {
            e.printStackTrace();
        }
    }





}

