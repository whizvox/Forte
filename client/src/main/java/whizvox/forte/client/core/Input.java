package whizvox.forte.client.core;

import org.lwjgl.glfw.GLFWKeyCallback;

public final class Input {

    public static void init() {
        ForteClient.getWindow().setCallback(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                Input.handle(key, action, mods);
            }
        });
    }

    private static void handle(int key, int action, int mods) {

        // TODO: Input profiles

    }

}
