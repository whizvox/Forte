package whizvox.forte.client.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharModsCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import whizvox.forte.client.core.ForteClient;

import java.util.HashMap;
import java.util.Map;

public final class Input {

    private static long window;
    private static boolean shift, ctrl, alt, supre;
    private static Map<String, InputProfile> profiles = new HashMap<>();
    private static InputProfile currentProfile = null;

    public static void init() {
        window = ForteClient.getWindow().getId();
        ForteClient.getWindow().setCallback(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                currentProfile.handle(key, action, mods);
            }
        });
        ForteClient.getWindow().setCallback(new GLFWCharModsCallback() {
            @Override
            public void invoke(long window, int codepoint, int mods) {

            }
        });
    }

    public static void poll() {
        shift = metaKeyHeld(GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);
        ctrl = metaKeyHeld(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL);
        alt = metaKeyHeld(GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT);
        supre = metaKeyHeld(GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_RIGHT_SUPER);
    }

    private static boolean metaKeyHeld(int key1, int key2) {
        return GLFW.glfwGetKey(window, key1) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(window, key2) == GLFW.GLFW_PRESS;
    }

    public static boolean isShiftHeld() {
        return shift;
    }

    public static boolean isCtrlHeld() {
        return ctrl;
    }

    public static boolean isAltHeld() {
        return alt;
    }

    public static boolean isSuperHeld() {
        return supre;
    }

}
