package whizvox.forte.client.core;

import whizvox.forte.client.render.GLFWUtil;
import whizvox.forte.client.render.Window;

public class ClientWindow extends Window {

    public ClientWindow() {
        Settings settings = ForteClient.getSettings();
        setSize(settings.getWinWidth(), settings.getWinHeight());
        setVisible(false);
        setDecorated(settings.isDecorated());
        setFullscreen(settings.isFullscreen());
        setMonitor(GLFWUtil.getPrimaryMonitor());
        setTitle("Forte ForteClient DEV-1.0");
        create();
    }

}
