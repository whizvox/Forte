package whizvox.forte.client.core;

import whizvox.forte.client.input.Input;
import whizvox.forte.client.render.*;
import whizvox.forte.common.Timer;

import java.util.HashMap;
import java.util.Map;

public class RenderTimer extends Timer {

    private static final Map<String, Object> configDefaults = new HashMap<>();

    static {
        configDefaults.put(Reference.ConfigKeys.WINDOW_POSX, 0);
        configDefaults.put(Reference.ConfigKeys.WINDOW_POSY, 0);
        configDefaults.put(Reference.ConfigKeys.WINDOW_FULLSCREEN, true);
        configDefaults.put(Reference.ConfigKeys.WINDOW_DECORATED, true);
    }

    private Renderer renderer;
    private Window window;

    public RenderTimer() {
        super(1);
    }

    @Override
    public void begin() {
        GLFWUtil.init();
        Monitor primaryMonitor = GLFWUtil.getPrimaryMonitor();
        setCap(primaryMonitor.refreshRate);
        if (!configDefaults.containsKey(Reference.ConfigKeys.WINDOW_WIDTH)) {
            configDefaults.put(Reference.ConfigKeys.WINDOW_WIDTH, primaryMonitor.width);
            configDefaults.put(Reference.ConfigKeys.WINDOW_HEIGHT, primaryMonitor.height);
        }
        window = new ClientWindow();
        renderer = new Renderer();
        renderer.init(1024);
        Input.init();
        setCap(ForteClient.getWindow().getMonitor().refreshRate);
        ForteClient.getSettings().update(configDefaults);
        ForteClient.getSettings().save();
    }

    @Override
    public void loop() {
        GLUtil.clear();
        Input.poll();

        window.update();
        if (window.shouldClose()) {
            ForteClient.stop();
        }
    }

    @Override
    public void end() {
        window.destroy();
        GLFWUtil.destroy();
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public Window getWindow() {
        return window;
    }

}
