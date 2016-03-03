package whizvox.forte.client.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import whizvox.forte.client.core.ForteClient;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
        Field[] fields = InputProfiles.class.getFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers()) && f.getName().startsWith("PROFILE_")) {
                String name = f.getName().substring(f.getName().indexOf('_') + 1).toLowerCase();
                try {
                    addProfile(name, (InputProfile) f.get(null));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (ForteClient.DEBUG) {
            setCurrentProfile("debug");
        } else {
            profiles.remove("debug");
        }
    }

    public static void poll() {
        shift = metaKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);
        ctrl = metaKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL);
        alt = metaKeyPressed(GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT);
        supre = metaKeyPressed(GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_RIGHT_SUPER);
    }

    public static void addProfile(String key, InputProfile profile) {
        profiles.put(key, profile);
    }

    public static void setCurrentProfile(String key) {
        currentProfile = profiles.get(key);
    }

    private static boolean metaKeyPressed(int key1, int key2) {
        return GLFW.glfwGetKey(window, key1) == GLFW.GLFW_PRESS || GLFW.glfwGetKey(window, key2) == GLFW.GLFW_PRESS;
    }

    private static boolean metaKeyHeld(int mods, int bits) {
        return (mods & bits) != 0;
    }

    public static boolean isShiftPressed() {
        return shift;
    }

    public static boolean isCtrlPressed() {
        return ctrl;
    }

    public static boolean isAltPressed() {
        return alt;
    }

    public static boolean isSuperPressed() {
        return supre;
    }

    public static boolean isShiftHeld(int mods) {
        return metaKeyHeld(mods, 0b1);
    }

    public static boolean isCtrlHeld(int mods) {
        return metaKeyHeld(mods, 0b10);
    }

    public static boolean isAltHeld(int mods) {
        return metaKeyHeld(mods, 0b100);
    }

    public static boolean isSuperHeld(int mods) {
        return metaKeyHeld(mods, 0b1000);
    }

}
