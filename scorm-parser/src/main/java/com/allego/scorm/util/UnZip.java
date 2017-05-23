package com.allego.scorm.util;

/**
 * Created by e_walker on 20.05.17.
 */


import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZip {
    private String pathToZip;
    public UnZip(String inputZipFile) {
        pathToZip = inputZipFile;

    }
    public byte[] getFileContent(String fileName) throws IOException {
        byte[] result = null;
        ZipInputStream zis =
                new ZipInputStream(new FileInputStream(fileName));
        byte[] data = new byte [16384];

            ZipEntry ze = zis.getNextEntry();

            while(ze!=null) {
                if (fileName.equals(ze.getName())) {
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    int len = 0;
                    while ((len = zis.read(data)) > 0) {
                        buffer.write(data,0,len);
                    }
                    if (buffer.size()>0) {
                        result = buffer.toByteArray();
                    }


                }
            }
            zis.closeEntry();
            zis.close();


        return result;
    }
}

