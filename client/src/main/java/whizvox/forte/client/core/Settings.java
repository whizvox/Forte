package whizvox.forte.client.core;

import whizvox.forte.common.Props;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static whizvox.forte.client.core.Reference.ConfigKeys.*;

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
    private float
            volumeMaster,
            volumeSfx,
            volumeMusic,
            sensitivity;
    private String
            lastSkin,
            username;
    private byte[]
            password;

    public Settings() {
        this.props = new Props();
    }

    private static final Set<String> CONFIG_KEYS;

    static {
        Field[] fields = Reference.ConfigKeys.class.getFields();
        CONFIG_KEYS = new HashSet<>();
        for (Field f : fields) {
            try {
                CONFIG_KEYS.add((String) f.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void update(Map<String, Object> defaults) {
        for (String key : CONFIG_KEYS) {
            Object def = defaults.get(key);
            if (def == null) {
                continue;
            }
            switch (key) {
                case GENERAL_CLEANUP_LOGS:
                    cleanupLogs = props.get(key, (Boolean) def);
                    break;
                case GENERAL_LOGS_SIZE_FLAG:
                    logsSizeFlag = props.get(key, (Integer) def);
                    break;
                case GENERAL_USERNAME:
                    username = props.get(key, (String) def);
                    break;
                case GENERAL_PASSWORD:
                    password = props.get(key, (byte[]) def);
                    break;
                case WINDOW_WIDTH:
                    winWidth = props.get(key, (Integer) def);
                    break;
                case WINDOW_HEIGHT:
                    winHeight = props.get(key, (Integer) def);
                    break;
                case WINDOW_POSX:
                    winPosX = props.get(key, (Integer) def);
                    break;
                case WINDOW_POSY:
                    winPosY = props.get(key, (Integer) def);
                    break;
                case WINDOW_FULLSCREEN:
                    fullscreen = props.get(key, (Boolean) def);
                    break;
                case WINDOW_DECORATED:
                    decorated = props.get(key, (Boolean) def);
                    break;
                case GAMEPLAY_SENSITIVITY:
                    sensitivity = props.get(key, (Float) def);
                    break;
                case GAMEPLAY_VOLUME_MASTER:
                    volumeMaster = props.get(key, (Float) def);
                    break;
                case GAMEPLAY_VOLUME_SFX:
                    volumeSfx = props.get(key, (Float) def);
                    break;
                case GAMEPLAY_VOLUME_MUSIC:
                    volumeMusic = props.get(key, (Float) def);
                    break;
                case GAMEPLAY_LAST_SKIN:
                    lastSkin = props.get(key, (String) def);
                    break;
            }
        }
    }

    public void save() {
        props.save(Files.getSettingsFile());
    }

    public void load() {
        props.read(Files.getSettingsFile());
    }

    public Props getProps() {
        return props;
    }

    public boolean canCleanupLogs() {
        return cleanupLogs;
    }

    public int getLogsSizeFlag() {
        return logsSizeFlag;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getPassword() {
        return password;
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

    public float getMasterVolume() {
        return volumeMaster;
    }

    public float getSfxVolume() {
        return volumeSfx;
    }

    public float getMusicVolume() {
        return volumeMusic;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public String getLastSkin() {
        return lastSkin;
    }

}
