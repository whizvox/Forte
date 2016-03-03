package whizvox.forte.client.core;

import whizvox.forte.client.render.GLFWUtil;
import whizvox.forte.client.render.Monitor;
import whizvox.forte.client.render.Window;
import whizvox.forte.common.Props;

public class ClientWindow extends Window {

    public ClientWindow() {
        Props props = ForteClient.getSettings().getProps();
        Monitor primaryMonitor = GLFWUtil.getPrimaryMonitor();
        int width = props.get(Reference.ConfigKeys.WINDOW_WIDTH, primaryMonitor.width);
        int height = props.get(Reference.ConfigKeys.WINDOW_HEIGHT, primaryMonitor.height);
        int posX = props.get(Reference.ConfigKeys.WINDOW_POSX, 0);
        int posY = props.get(Reference.ConfigKeys.WINDOW_POSY, 0);
        boolean fullscreen = props.get(Reference.ConfigKeys.WINDOW_FULLSCREEN, true);
        boolean decorated = props.get(Reference.ConfigKeys.WINDOW_DECORATED, true);
        setSize(width, height);
        setVisible(false);
        setDecorated(decorated);
        setFullscreen(fullscreen);
        setMonitor(GLFWUtil.getPrimaryMonitor());
        setPosition(posX, posY);
        setTitle(ForteClient.NAME + " " + ForteClient.VERSION);
        create();
        setVisible(true);
    }

}
