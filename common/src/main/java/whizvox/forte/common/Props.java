package whizvox.forte.common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Props {

    private static Map<String, String> entries = new HashMap<>();

    public Props() {

    }

    public void set(String key, String value) {
        entries.put(key, value);
    }

    public void set(String key, Object obj) {
        set(key, String.valueOf(obj));
    }

    public String getObject(String key, Object def) {
        if (entries.containsKey(key)) {
            return entries.get(key);
        }
        String value = String.valueOf(def);
        set(key, value);
        return value;
    }

    public String get(String key, String def) {
        return getObject(key, def);
    }

    public byte get(String key, byte def) {
        return Byte.parseByte(getObject(key, def));
    }

    public short get(String key, short def) {
        return Short.parseShort(getObject(key, def));
    }

    public int get(String key, int def) {
        return Integer.parseInt(getObject(key, def));
    }

    public long get(String key, long def) {
        return Long.parseLong(getObject(key, def));
    }

    public float get(String key, float def) {
        return Float.parseFloat(getObject(key, def));
    }

    public double get(String key, double def) {
        return Double.parseDouble(getObject(key, def));
    }

    public boolean get(String key, boolean def) {
        return StringUtils.parseBoolean(getObject(key, def));
    }

    public void save(OutputStream out) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out))) {
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                writer.write(entry.getKey());
                writer.write('=');
                writer.write(String.valueOf(entry.getValue()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(File file) {
        try {
            save(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void read(InputStream in) {
        entries.clear();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = tokenize(line);
                if (tokens != null) {
                    set(tokens[0], tokens[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(File file) {
        try {
            read(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String[] tokenize(String line) {
        int indexOf = line.indexOf('=');
        if (indexOf != -1 && indexOf < line.length() - 1) {
            String key = line.substring(0, indexOf);
            String value = line.substring(indexOf + 1);
            return new String[] {key, value};
        }
        return null;
    }

}
