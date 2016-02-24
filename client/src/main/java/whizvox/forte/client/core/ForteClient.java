package whizvox.forte.client.core;

import whizvox.forte.client.render.GLUtil;
import whizvox.forte.client.render.Window;
import whizvox.forte.common.Logger;

import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ForteClient implements Runnable {

    private Logger logger;
    private Window window;
    private Settings settings;
    private final File root;

    public ForteClient(File root) {
        this.root = root;
    }

    @Override
    public void run() {
        try {
            Files.init(root);
            logger = new Logger(System.out, "ForteClient", new PrintStream(Files.getLogFile()));
            System.setOut(logger);
            System.setErr(logger);
            settings = new Settings();
            window = new Window();
            window.setTitle("Forte Client (Dev-1.0)");
            window.setSize(settings.getWinWidth(), settings.getWinHeight());
            window.setVisible(false);
            window.setFullscreen(settings.isFullscreen());
            window.setDecorated(settings.isDecorated());
            window.create();
            window.setVisible(true);

            Input.init();

            while (!window.shouldClose()) {
                loop();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void loop() {
        GLUtil.clear();



        window.update();
    }

    /* ========== STATIC STUFF ========== */

    private static ForteClient instance = null;

    public static Settings getSettings() {
        return instance.settings;
    }

    public static Logger getLogger() {
        return instance.logger;
    }

    public static Window getWindow() {
        return instance.window;
    }

    private static String getStringFromArray(String[] array, int offset) {
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < array.length; i++) {
            String arg = array[i];
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

    private static File getDefaultRootDir() {
        String osName = System.getProperty("os.name");
        String userDir = System.getProperty("user.home");
        Path rootDir;
        if (osName.startsWith("Windows")) {
            if (!osName.contains("XP")) {
                rootDir = Paths.get(userDir, "AppData", "Local", "Forte");
            } else {
                rootDir = Paths.get(userDir, "AppData", "Forte");
            }
        } else {
            rootDir = Paths.get(userDir, "Forte");
        }
        return rootDir.toFile();
    }

    public static void main(String[] args) {
        File root = null;
        if (args.length > 1) {
            if (args[0].equals("-r")) {
                root = new File(getStringFromArray(args, 1));
            }
        }
        if (root == null) {
            root = getDefaultRootDir();
        }
        instance = new ForteClient(root);
        new Thread(instance).start();
    }

}
