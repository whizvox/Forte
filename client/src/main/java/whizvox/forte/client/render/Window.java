package whizvox.forte.client.render;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width = 1, height = 1;
    private long window = NULL;
    private boolean initialized = false, fullscreen = false, vsync = true, resizazble = false, decorated = true, visible = true, dirty = false;
    private CharSequence title = "GLFW Window";
    private Monitor monitor = null;

    public Window() {

    }

    public void create() {
        if (initialized) {
            return;
        }

        GLFWUtil.init();
        if (monitor == null) {
            monitor = GLFWUtil.getPrimaryMonitor();
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFWUtil.cbool(visible));
        glfwWindowHint(GLFW_DECORATED, GLFWUtil.cbool(decorated));
        glfwWindowHint(GLFW_RESIZABLE, GLFWUtil.cbool(resizazble));
        if (GLFWUtil.supportsModernGL()) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        } else if (GLFWUtil.supportsLegacyGL()) {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        } else {
            throw new RuntimeException("Neither OpenGL 2.1 nor 3.2 is supported. You may need to update your video driver.");
        }
        if (fullscreen) {
            window = glfwCreateWindow(monitor.width, monitor.height, title, monitor.handle, NULL);
        } else {
            window = glfwCreateWindow(width, height, title, NULL, NULL);
        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(GLFWUtil.cbool(vsync));

        GL.setCapabilities(GLFWUtil.getGLCapabilities());
        configureOpenGL();
    }

    public void configureOpenGL() {}

    public void onDirty() {
        destroy();
        create();
    }

    public void destroy() {
        glfwDestroyWindow(window);
        initialized = false;
        window = NULL;
        monitor = null;
    }

    public void update() {
        if (dirty) {
            onDirty();
        }
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setSize(int width, int height) {
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            if (initialized) {
                glfwSetWindowSize(window, width, height);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setFullscreen(boolean fullscreen) {
        if (this.fullscreen != fullscreen) {
            this.fullscreen = fullscreen;
            if (initialized) {
                dirty = true;
            }
        }
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setVsync(boolean vsync) {
        if (this.vsync != vsync) {
            this.vsync = vsync;
            if (initialized) {
                glfwSwapInterval(GLFWUtil.cbool(vsync));
            }
        }
    }

    public boolean getVsync() {
        return vsync;
    }

    public void setResizazble(boolean resizazble) {
        if (this.resizazble != resizazble) {
            this.resizazble = resizazble;
            if (initialized) {
                dirty = true;
            }
        }
    }

    public boolean isResizable() {
        return resizazble;
    }

    public void setDecorated(boolean decorated) {
        if (this.decorated != decorated) {
            this.decorated = decorated;
            if (initialized) {
                dirty = true;
            }
        }
    }

    public boolean isDecorated() {
        return decorated;
    }

    public void setVisible(boolean visible) {
        if (this.visible != visible) {
            this.visible = visible;
            if (initialized) {
                if (visible) {
                    glfwShowWindow(window);
                } else {
                    glfwHideWindow(window);
                }
            }
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setMonitor(Monitor monitor) {
        if (!this.monitor.equals(monitor)) {
            this.monitor = monitor;
            if (initialized) {
                this.dirty = true;
            }
        }
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public void setTitle(CharSequence title) {
        if (!this.title.equals(title)) {
            this.title = title;
            if (initialized) {
                glfwSetWindowTitle(window, title);
            }
        }
    }

    public CharSequence getTitle() {
        return title;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window) == GLFW_TRUE;
    }

    public void setShouldClose(boolean shouldClose) {
        if ((shouldClose() != shouldClose) && initialized) {
            glfwSetWindowShouldClose(window, GLFWUtil.cbool(shouldClose));
        }
    }

    public long getId() {
        return window;
    }

    public void setCallback(Object callback) {
        if (callback instanceof GLFWCharCallback) {
            glfwSetCharCallback(window, (GLFWCharCallback) callback);
        } else if (callback instanceof GLFWDropCallback) {
            glfwSetDropCallback(window, (GLFWDropCallback) callback);
        } else if (callback instanceof GLFWKeyCallback) {
            glfwSetKeyCallback(window, (GLFWKeyCallback) callback);
        } else if (callback instanceof GLFWScrollCallback) {
            glfwSetScrollCallback(window, (GLFWScrollCallback) callback);
        } else if (callback instanceof GLFWCharModsCallback) {
            glfwSetCharModsCallback(window, (GLFWCharModsCallback) callback);
        } else if (callback instanceof GLFWCursorEnterCallback) {
            glfwSetCursorEnterCallback(window, (GLFWCursorEnterCallback) callback);
        } else if (callback instanceof GLFWCursorPosCallback) {
            glfwSetCursorPosCallback(window, (GLFWCursorPosCallback) callback);
        } else if (callback instanceof GLFWFramebufferSizeCallback) {
            glfwSetFramebufferSizeCallback(window, (GLFWFramebufferSizeCallback) callback);
        } else if (callback instanceof GLFWMouseButtonCallback) {
            glfwSetMouseButtonCallback(window, (GLFWMouseButtonCallback) callback);
        } else if (callback instanceof GLFWWindowCloseCallback) {
            glfwSetWindowCloseCallback(window, (GLFWWindowCloseCallback) callback);
        } else if (callback instanceof GLFWWindowFocusCallback) {
            glfwSetWindowFocusCallback(window, (GLFWWindowFocusCallback) callback);
        } else if (callback instanceof GLFWWindowIconifyCallback) {
            glfwSetWindowIconifyCallback(window, (GLFWWindowIconifyCallback) callback);
        } else if (callback instanceof GLFWWindowPosCallback) {
            glfwSetWindowPosCallback(window, (GLFWWindowPosCallback) callback);
        } else if (callback instanceof GLFWWindowRefreshCallback) {
            glfwSetWindowRefreshCallback(window, (GLFWWindowRefreshCallback) callback);
        } else if (callback instanceof GLFWWindowSizeCallback) {
            glfwSetWindowSizeCallback(window, (GLFWWindowSizeCallback) callback);
        }
    }

}
