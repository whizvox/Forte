package whizvox.forte.client.render;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private int width, height, posX, posY;
    private long window;
    private boolean initialized, fullscreen, vsync, resizable, decorated, visible, shouldClose, focused, dirty;
    private CharSequence title;
    private Monitor monitor;

    /**
     * A non-zero mark means that some setting has been changed, but doesn't require a destruction and recreation of
     * the window. This includes changing the size, vsync status, visibility, title, or close status.
     */
    private short marked = 0;

    private static final byte
            flag_size =     0b000001,
            flag_vsync =    0b000010,
            flag_visible =  0b000100,
            flag_title =    0b001000,
            flag_close =    0b010000,
            flag_position = 0b100000;

    public Window() {

    }

    /**
     * Only call in the rendering method!
     */
    public void create() {
        if (initialized) {
            return;
        }

        if (monitor == null) {
            monitor = GLFWUtil.getPrimaryMonitor();
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFWUtil.cbool(visible));
        glfwWindowHint(GLFW_DECORATED, GLFWUtil.cbool(decorated));
        glfwWindowHint(GLFW_RESIZABLE, GLFWUtil.cbool(resizable));
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
            glfwSetWindowPos(window, posX, posY);
        }
        glfwMakeContextCurrent(window);
        focused = true;
        glfwSwapInterval(GLFWUtil.cbool(vsync));

        setCallback(new GLFWWindowCloseCallback() {
            @Override
            public void invoke(long window) {
                if (window == Window.this.window) {
                    shouldClose = true;
                }
            }
        });
        setCallback(new GLFWWindowPosCallback() {
            @Override
            public void invoke(long window, int xpos, int ypos) {
                if (window == Window.this.window) {
                    posX = xpos;
                    posY = ypos;
                }
            }
        });
        setCallback(new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, int focused) {
                if (window == Window.this.window) {
                    Window.this.focused = GLFWUtil.jbool(focused);
                }
            }
        });

        GL.setCapabilities(GLFWUtil.getGLCapabilities());
        configureOpenGL();

        initialized = true;
    }

    /**
     * Only call in the rendering method!
     */
    public void configureOpenGL() {}

    protected void onDirty() {
        destroy();
        create();
    }

    /**
     * Only call in the rendering method!
     */
    public void destroy() {
        glfwDestroyWindow(window);
        initialized = false;
        window = NULL;
        monitor = null;
    }

    /**
     * Only call in the rendering thread!
     */
    public void update() {
        if (marked != 0) {
            if ((marked & flag_size) != 0) {
                glfwSetWindowSize(window, width, height);
                marked &= ~flag_size;
            }
            if ((marked & flag_vsync) != 0) {
                glfwSwapInterval(GLFWUtil.cbool(vsync));
                marked &= ~flag_vsync;
            }
            if ((marked & flag_visible) != 0) {
                if (visible) {
                    glfwShowWindow(window);
                } else {
                    glfwHideWindow(window);
                }
                marked &= ~flag_visible;
            }
            if ((marked & flag_title) != 0) {
                glfwSetWindowTitle(window, title);
                marked &= ~flag_title;
            }
            if ((marked & flag_close) != 0) {
                glfwSetWindowShouldClose(window, GLFWUtil.cbool(shouldClose()));
                marked &= ~flag_close;
            }
            if ((marked & flag_position) != 0) {
                glfwSetWindowPos(window, posX, posY);
                marked &= ~flag_position;
            }
        }
        if (dirty) {
            onDirty();
        }
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    /**
     * Only call in the rendering thread!
     * @param callback A GLFW callback.
     */
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

    /* ================== Every other method is multi-thread friendly. ================== */

    /**
     * A "dirty status" means that some setting has been changed so that the window is required to be restarted in
     * order to apply that change.
     * @return Whether or not the window is dirty.
     */
    public boolean isDirty() {
        return dirty;
    }

    public void setSize(int width, int height) {
        if (this.width != width || this.height != height) {
            if (width < 1 || height < 1) {
                throw new IllegalArgumentException("Width nor height cannot be less than 1: " + width + ", " + height);
            }
            this.width = width;
            this.height = height;
            if (initialized) {
                marked |= flag_size;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(int posX, int posY) {
        if (this.posX != posX || this.posY != posY) {
            this.posX = posX;
            this.posY = posY;
            if (initialized) {
                marked |= flag_position;
            }
        }
    }

    public void setCenterPosition() {
        setPosition((monitor.width - width) / 2, (monitor.height - height) / 2);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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
                marked |= flag_vsync;
            }
        }
    }

    public boolean getVsync() {
        return vsync;
    }

    public void setResizable(boolean resizable) {
        if (this.resizable != resizable) {
            this.resizable = resizable;
            if (initialized) {
                dirty = true;
            }
        }
    }

    public boolean isResizable() {
        return resizable;
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
                marked |= flag_visible;
            }
        }
    }

    public boolean isVisible() {
        return visible;
    }

    public void setMonitor(Monitor monitor) {
        if (monitor == null || monitor.handle == NULL) {
            throw new NullPointerException("Monitor nor monitor handle can be null: " + String.valueOf(monitor));
        }
        if (!monitor.equals(this.monitor)) {
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
        if (title == null || title.length() == 0) {
            throw new NullPointerException("title cannot be null nor empty!");
        }
        if (!title.equals(this.title)) {
            this.title = title;
            if (initialized) {
                marked |= flag_title;
            }
        }
    }

    public CharSequence getTitle() {
        return title;
    }

    public boolean shouldClose() {
        return shouldClose;
    }

    public void setShouldClose(boolean shouldClose) {
        if (this.shouldClose != shouldClose) {
            this.shouldClose = shouldClose;
            if (initialized) {
                marked |= flag_close;
            }
        }
    }

    public boolean isFocused() {
        return focused;
    }

    public long getId() {
        return window;
    }

}
