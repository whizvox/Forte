package whizvox.forte.common;

import java.io.File;

public abstract class Application implements Runnable {

    private File root = null;

    protected Application() {}

    protected abstract void init(String[] progArgs);

    public final void begin(String[] args) {
        init(args);
        // TODO: More to this
    }

    public static String concatStringArgs(String[] args, int offset) {
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < args.length; i++) {
            String arg = args[i];
            int indexOf = arg.indexOf('"');
            if (indexOf != -1) {
                int indexOf2 = arg.indexOf('"', indexOf + 1);
                if (indexOf2 != -1) {
                    sb.append(arg.substring(indexOf + 1, indexOf2));
                    break;
                } else {
                    if (sb.length() != 0) {
                        sb.append(arg.substring(0, indexOf));
                        break;
                    } else {
                        sb.append(arg.substring(indexOf + 1));
                    }
                }
            } else {
                sb.append(arg);
            }
            sb.append(' ');
        }
        return sb.toString();
    }

    public static void start(String[] args, Application application) {
        application.init(args);
        new Thread(application).start();
    }

}
