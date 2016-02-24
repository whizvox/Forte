package whizvox.forte.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IOUtils {

    public static void touchFile(File file) {
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static InputStream getInternalResource(String path) {
        return IOUtils.class.getClassLoader().getResourceAsStream(path);
    }

}
