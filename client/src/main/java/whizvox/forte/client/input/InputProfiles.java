package whizvox.forte.client.input;

import org.lwjgl.glfw.GLFW;
import whizvox.forte.client.core.ForteClient;

public class InputProfiles {

    private InputProfiles() {}

    public static final InputProfile PROFILE_DEBUG = (key, action, mods) -> {
        if (key == GLFW.GLFW_KEY_ESCAPE) {
            ForteClient.getWindow().setShouldClose(true);
        }
    };

}
