package whizvox.forte.client.render;

import org.lwjgl.opengl.GL30;

public class VertexArrayObject {

    private int id;

    public VertexArrayObject() {
        id = GL30.glGenFramebuffers();
    }

    public void bind() {
        GL30.glBindVertexArray(id);
    }

    public void delete() {
        GL30.glDeleteVertexArrays(id);
    }

    public int getId() {
        return id;
    }

}
