package whizvox.forte.client.core;

import whizvox.forte.common.Timer;

import java.util.HashMap;
import java.util.Map;

public class UpdateTimer extends Timer {

    private static final Map<String, Object> configDefaults = new HashMap<>();

    static {
        configDefaults.put(Reference.ConfigKeys.GAMEPLAY_LAST_SKIN, "default");
        configDefaults.put(Reference.ConfigKeys.GAMEPLAY_SENSITIVITY, 1f);
        configDefaults.put(Reference.ConfigKeys.GAMEPLAY_VOLUME_MASTER, 0.5f);
        configDefaults.put(Reference.ConfigKeys.GAMEPLAY_VOLUME_SFX, 1f);
        configDefaults.put(Reference.ConfigKeys.GAMEPLAY_VOLUME_MUSIC, 1f);
        configDefaults.put(Reference.ConfigKeys.GENERAL_CLEANUP_LOGS, true);
        configDefaults.put(Reference.ConfigKeys.GENERAL_LOGS_SIZE_FLAG, 25);
        configDefaults.put(Reference.ConfigKeys.GENERAL_USERNAME, "");
        configDefaults.put(Reference.ConfigKeys.GENERAL_PASSWORD, new byte[0]);
    }

    public UpdateTimer() {
        super(1000);
    }

    @Override
    public void begin() {
        ForteClient.getSettings().update(configDefaults);
        ForteClient.getSettings().save();
    }

    @Override
    public void loop() {

    }

    @Override
    public void end() {

    }

}
