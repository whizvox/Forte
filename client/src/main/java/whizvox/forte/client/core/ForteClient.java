package whizvox.forte.client.core;

import whizvox.forte.client.input.Input;
import whizvox.forte.client.render.GLUtil;
import whizvox.forte.client.render.Window;
import whizvox.forte.common.Application;
import whizvox.forte.common.Logger;
import whizvox.forte.common.Props;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ForteClient extends Application {

    private Logger logger;
    private Window window;
    private Settings settings;
    private File root = null;

    @Override
    public void run() {
        try {
            Files.init(root);
            logger = new ClientLogger();
            settings = new Settings();
            window = new ClientWindow();
            Input.init();
            while (!window.shouldClose()) {
                loop();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    protected void init(Props parameters) {
        if (parameters.length > 1) {
            if (parameters[0].equals("-r")) {
                root = new File(Application.concatStringArgs(parameters, 1));
            }
        }
        if (root == null) {
            root = getDefaultRootDir();
        }
    }

    public void loop() {
        GLUtil.clear();
        Input.poll();
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

    public static void main(Props args) {
        (instance = new ForteClient()).begin(args);
    }

}
