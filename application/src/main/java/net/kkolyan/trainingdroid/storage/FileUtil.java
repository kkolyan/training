package net.kkolyan.trainingdroid.storage;

import java.io.File;

/**
 * @author nplekhanov
 */
public class FileUtil {
    public static void mkdir(File dir) {
        for (int i = 0; i < 10; i ++) {
            if (dir.mkdirs()) {
                return;
            }
        }
        if (dir.exists()) {
            return;
        }
        throw new IllegalStateException("can't create directory: '"+dir+"'");
    }
}
