package com.sf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Shenfan on 2017/7/12.
 */
public class FileAccess {

    static String filePath = getAbsPath();

    static void writeToFile(String content) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, true);
            fileWriter.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String getAbsPath() {
        File file = new File("");
        return file.getAbsolutePath();
    }
}
