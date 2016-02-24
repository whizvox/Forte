package whizvox.forte.client.render;

import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;

public class VertexBufferObject {

    public static final int
        TARGET_ARRAY_BUFFER = GL15.GL_ARRAY_BUFFER,
        USAGE_STATIC_DRAW = GL15.GL_STATIC_DRAW,
        USAGE_STATIC_READ = GL15.GL_STATIC_READ,
        USAGE_STATIC_COPY = GL15.GL_STATIC_COPY,
        USAGE_DYNAMIC_DRAW = GL15.GL_DYNAMIC_DRAW,
        USAGE_DYNAMIC_READ = GL15.GL_DYNAMIC_READ,
        USAGE_DYNAMIC_COPY = GL15.GL_DYNAMIC_COPY;

    private int id;

    public VertexBufferObject() {
        id = GL15.glGenBuffers();
    }

    public void bind(int target) {
        GL15.glBindBuffer(target, id);
    }

    public void bind() {
        GL15.glBindBuffer(TARGET_ARRAY_BUFFER, id);
    }

    public void delete() {
        GL15.glDeleteBuffers(id);
    }

    public int getId() {
        return id;
    }

    public void uploadData(int target, FloatBuffer data, int usage) {
        GL15.glBufferData(target, data, usage);
    }

    public void uploadData(FloatBuffer data, int usage) {
        uploadData(TARGET_ARRAY_BUFFER, data, usage);
    }

    public void uploadData(int target, long offset, int usage) {
        GL15.glBufferData(target, offset, usage);
    }

    public void uploadData(long offset, int usage) {
        uploadData(TARGET_ARRAY_BUFFER, offset, usage);
    }

    public void uploadSubData(int target, long offset, FloatBuffer data) {
        GL15.glBufferSubData(target, offset, data);
    }

    public void uploadSubData(long offset, FloatBuffer data) {
        uploadSubData(TARGET_ARRAY_BUFFER, offset, data);
    }

    public void uploadSubData(FloatBuffer data) {
        uploadSubData(TARGET_ARRAY_BUFFER, 0L, data);
    }

}
