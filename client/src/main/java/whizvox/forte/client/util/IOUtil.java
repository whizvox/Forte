package whizvox.forte.client.util;

import java.io.InputStream;

public class IOUtil {

    public static InputStream getInternalResource(String path) {
        return IOUtil.class.getClassLoader().getResourceAsStream(path);
    }

}
