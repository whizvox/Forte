package whizvox.forte.common;

import java.io.File;

public abstract class Application implements Runnable {

    private File root = null;

    protected Application() {}

    protected abstract void init(Props parameters);

    public final void begin(String[] args) {
        Props parameters = convertParametersToProps(args);
        root = new File(parameters.get("root", "."));
        init(parameters);
        new Thread(this).start();
    }

    public final File getRoot() {
        return root;
    }

    public static Props convertParametersToProps(String[] params) {
        Props props = new Props();
        StringBuilder sb = new StringBuilder();
        for (String par : params) {
            sb.append(par).append(' ');
        }
        String s = sb.toString();
        String key = null, value = null;
        int first = 0;
        boolean openQuote = false;
        for (int i = 0; i < s.toCharArray().length; i++) {
            char c = s.charAt(i);
            if (c == '"') {
                if (openQuote) {
                    value = s.substring(first, i);
                    openQuote = false;
                    if (key != null) {
                        props.set(key, value);
                        key = null;
                        value = null;
                    }
                } else {
                    first = i + 1;
                    openQuote = true;
                }
            } else if (c == '=') {
                if (!openQuote) {
                    key = s.substring(first, i);
                    first = i + 1;
                }
            } else if (c == ' ') {
                if (!openQuote) {
                    if (key != null) {
                        value = s.substring(first, i);
                        props.set(key, value);
                        key = null;
                        value = null;
                        first = i + 1;
                    }
                }
            }
        }
        return props;
    }

}
