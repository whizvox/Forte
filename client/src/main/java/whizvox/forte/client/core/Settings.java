package whizvox.forte.client.core;

import whizvox.forte.common.Props;

public class Settings {

    private Props props;

    private int
            logsSizeFlag,
            winWidth,
            winHeight,
            winPosX,
            winPosY;

    private boolean
            cleanupLogs,
            fullscreen,
            decorated;

    public Settings() {
        this.props = new Props();

        props.read(Files.getSettingsFile());

        logsSizeFlag = props.get("general.logsSizeFlag", 25);
        cleanupLogs = props.get("general.cleanupLogs", true);

        winWidth = props.get("window.width", -1);
        winHeight = props.get("window.height", -1);
        winPosX = props.get("window.x", 0);
        winPosY = props.get("window.y", 0);

        fullscreen = props.get("window.fullscreen", true);
        decorated = props.get("window.decorated", true);

        props.save(Files.getSettingsFile());
    }

    public boolean canCleanupLogs() {
        return cleanupLogs;
    }

    public int getLogsSizeFlag() {
        return logsSizeFlag;
    }

    public int getWinWidth() {
        return winWidth;
    }

    public int getWinHeight() {
        return winHeight;
    }

    public int getWinPosX() {
        return winPosX;
    }

    public int getWinPosY() {
        return winPosY;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public boolean isDecorated() {
        return decorated;
    }

}
