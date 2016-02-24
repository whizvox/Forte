package whizvox.forte.client.render;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;

public class GLFWUtil {

    private static List<Monitor> monitors = new ArrayList<>();
    private static int primaryMonitor = -1;
    private static GLCapabilities glCapabilities;
    private static boolean initialized = false;

    public static boolean isInitialized() {
        return initialized;
    }

    public static void init() {
        if (!initialized) {
            glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
            if (glfwInit() != GLFW_TRUE) {
                throw new IllegalStateException("Could not initialize GLFW!");
            }
            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            long temp = glfwCreateWindow(1, 1, "", NULL, NULL);
            glfwMakeContextCurrent(temp);
            PointerBuffer monBuffer = glfwGetMonitors();
            while (monBuffer.hasRemaining()) {
                long handle = monBuffer.get();
                GLFWVidMode vidMode = glfwGetVideoMode(handle);
                int width = vidMode.width();
                int height = vidMode.height();
                int refreshRate = vidMode.refreshRate();
                String name = glfwGetMonitorName(handle);
                if (handle == glfwGetPrimaryMonitor()) {
                    primaryMonitor = monitors.size();
                }
                monitors.add(new Monitor(handle, width, height, refreshRate, name));
            }
            glCapabilities = GL.createCapabilities();
            GL.destroy();
            glfwDestroyWindow(temp);
            initialized = true;
        }
    }

    public static void destroy() {
        if (initialized) {
            glfwTerminate();
            glCapabilities = null;
            monitors.clear();
            initialized = false;
        }
    }

    public static GLCapabilities getGLCapabilities() {
        return glCapabilities;
    }

    public static boolean supportsModernGL() {
        return glCapabilities.OpenGL32;
    }

    public static boolean supportsLegacyGL() {
        return glCapabilities.OpenGL21;
    }

    public static Iterable<Monitor> getMonitors() {
        return monitors;
    }

    public static Monitor getPrimaryMonitor() {
        if (primaryMonitor != -1) {
            return monitors.get(primaryMonitor);
        }
        return null;
    }

    public static int cbool(boolean b) {
        return b ? GLFW_TRUE : GLFW_FALSE;
    }

}
