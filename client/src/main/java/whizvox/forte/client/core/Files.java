package whizvox.forte.client.core;

import whizvox.forte.common.IOUtils;
import whizvox.forte.common.StringUtils;

import java.io.File;

public class Files {

    private static File rootDir, logsDir, replaysDir, skinsDir, log, settings, scores;

    public static void init(File root) {
        rootDir = root;
        logsDir = new File(root, "logs");
        replaysDir = new File(root, "replays");
        skinsDir = new File(root, "skins");
        log = new File(logsDir, StringUtils.getCompactTimestamp() + ".log");
        settings = new File(root, "settings.props");
        scores = new File(root, "scores.db");

        root.mkdirs();
        logsDir.mkdirs();
        replaysDir.mkdir();
        skinsDir.mkdir();
        IOUtils.touchFile(log);
        IOUtils.touchFile(settings);
        IOUtils.touchFile(scores);
    }

    public static File getRootDir() {
        return rootDir;
    }

    public static File getLogsDir() {
        return logsDir;
    }

    public static File getReplaysDir() {
        return replaysDir;
    }

    public static File getSkinsDir() {
        return skinsDir;
    }

    public static File getLogFile() {
        return log;
    }

    public static File getSettingsFile() {
        return settings;
    }

    public static File getScoresFile() {
        return scores;
    }

}
