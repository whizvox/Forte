package whizvox.forte.client.core;

import whizvox.forte.client.render.Window;
import whizvox.forte.common.Application;
import whizvox.forte.common.Logger;
import whizvox.forte.common.Props;
import whizvox.forte.common.Timer;

import java.io.FileNotFoundException;

public class ForteClient extends Application {

    public static final boolean DEBUG = true;
    public static final String NAME = "Forte", VERSION = "Dev-1.0";

    private Logger logger;
    private Settings settings;

    private UpdateTimer updateTimer;
    private RenderTimer renderTimer;

    @Override
    public void run() {
        Timer.start(updateTimer);
        Timer.start(renderTimer);
    }

    @Override
    protected void init(Props parameters) {
        Files.init(getRoot());
        settings = new Settings();
        settings.load();
        try {
            logger = new ClientLogger();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize logger!");
        }
        updateTimer = new UpdateTimer();
        renderTimer = new RenderTimer();
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
        return instance.renderTimer.getWindow();
    }

    public static void stop() {
        instance.renderTimer.setShouldStop(true);
        instance.updateTimer.setShouldStop(true);
    }

    public static void main(String[] args) {
        (instance = new ForteClient()).begin(args);
    }

}
