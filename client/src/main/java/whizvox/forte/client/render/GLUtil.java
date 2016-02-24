package whizvox.forte.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class GLUtil {

    public static void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void unbindTexture() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public static void unbindVBO() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public static void unbindVAO() {
        glBindVertexArray(0);
    }

}
