package com.allego.scorm;


import org.junit.Assert;


public class AppTest {

    private String baseDir = System.getProperty("user.dir") + "/suites/";

    @org.junit.Test
    public void testApp() {

        IScormParser parser = ScormParserFactory.getScormParser();

        try {
            parser.parse(baseDir + "scorm12/golf12.zip", "", "", "", "");
        } catch (BadSCORMPackageException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }
}
