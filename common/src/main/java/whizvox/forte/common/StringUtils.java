package whizvox.forte.common;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StringUtils {

    private static final SimpleDateFormat SDF_COMPACT = new SimpleDateFormat("yyyyMMddkkmmss");

    public static String getCompactTimestamp() {
        return SDF_COMPACT.format(new Date());
    }

    public static Date parseCompactTimestamp(String timestamp) {
        return SDF_COMPACT.parse(timestamp, new ParsePosition(0));
    }

    public static List<String> splitAtChar(String s, char c, int max) {
        final List<String> list = new ArrayList<>();
        int index, last = 0;
        while (max > list.size() - 1 && (index = s.indexOf(c, last)) != -1) {
            list.add(s.substring(last, index));
            last = index + 1;
        }
        list.add(s.substring(last));
        return list;
    }

    public static List<String> splitAtChar(String s, char c) {
        return splitAtChar(s, c, Integer.MAX_VALUE);
    }

    public static boolean parseBoolean(String s) {
        if (s != null && !s.isEmpty()) {
            if (s.equals("0") || s.equalsIgnoreCase("false")) {
                return false;
            } else if (s.equals("1") || s.equalsIgnoreCase("true")) {
                return true;
            }
        }
        throw new NumberFormatException("Could not parse boolean: " + s);
    }

    public static boolean isNotNullOrEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    public static String getFileExtension(String name) {
        final int index = name.lastIndexOf('.');
        if (index != -1) {
            return name.substring(index + 1);
        }
        return "";
    }

    public static String getFileNameWithoutExtension(String name) {
        int index = name.lastIndexOf('.');
        if (index != -1) {
            return name.substring(0, index);
        }
        return name;
    }

}
