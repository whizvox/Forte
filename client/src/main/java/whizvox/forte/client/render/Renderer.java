package whizvox.forte.client.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import whizvox.forte.client.core.ForteClient;
import whizvox.forte.client.math.Matrix4f;
import whizvox.forte.client.math.Vector2f;
import whizvox.forte.client.util.IOUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Renderer {

    private VertexArrayObject vao;
    private VertexBufferObject vbo;
    private Shader shader_vert, shader_frag;
    private ShaderProgram program;

    private FloatBuffer vertices;
    private int numVertices;
    private boolean drawing;

    public void init(int maxVertices) {
        boolean modern = GLFWUtil.supportsModernGL();
        boolean legacy = GLFWUtil.supportsLegacyGL();
        if (modern) {
            vao = new VertexArrayObject();
            vao.bind();
        } else if (legacy) {
            vao = null;
        } else {
            throw new RuntimeException("OpenGL 3.2 or 2.1 must be supported. You may need to update your video drivers.");
        }

        vbo = new VertexBufferObject();
        vbo.bind();

        vertices = BufferUtils.createFloatBuffer(maxVertices);

        long size = vertices.capacity() * Float.BYTES;
        vbo.uploadData(size, VertexBufferObject.USAGE_DYNAMIC_DRAW);

        numVertices = 0;
        drawing = false;

        if (modern) {
            shader_vert = Shader.loadVertex(IOUtil.getInternalResource("resources/default.vert"));
            shader_frag = Shader.loadFragment(IOUtil.getInternalResource("resources/default.frag"));
        } else {
            shader_vert = Shader.loadVertex(IOUtil.getInternalResource("resources/legacy.vert"));
            shader_frag = Shader.loadFragment(IOUtil.getInternalResource("resources/legacy.frag"));
        }

        program = new ShaderProgram();
        program.attachShader(shader_vert);
        program.attachShader(shader_frag);
        if (modern) {
            program.bindFragmentData(0, "fragColor");
        }
        program.link();
        program.use();

        long window = ForteClient.getWindow().getId();
        IntBuffer
                widthBuf = BufferUtils.createIntBuffer(1),
                heightBuf = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetFramebufferSize(window, widthBuf, heightBuf);
        int width = widthBuf.get();
        int height = heightBuf.get();

        specifyVertexAttributes();

        int uniTex = program.getUniformLocation("texImage");
        program.setUniform1i(uniTex, 0);

        Matrix4f modelMatrix = new Matrix4f();
        int uniModel = program.getUniformLocation("model");
        program.setUniformMatrix4f(uniModel, modelMatrix);

        Matrix4f viewMatrix = new Matrix4f();
        int uniView = program.getUniformLocation("view");
        program.setUniformMatrix4f(uniView, viewMatrix);

        Matrix4f projMatrix = Matrix4f.orthographic(0f, width, 0f, height, -1f, 1f);
        int uniProj = program.getUniformLocation("projection");
        program.setUniformMatrix4f(uniProj, projMatrix);
    }

    protected void specifyVertexAttributes() {
        int posAttrib = program.getAttributeLocation("position");
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 2, Float.SIZE * 7, 0);

        int colAttrib = program.getAttributeLocation("color");
        program.enableVertexAttribute(colAttrib);
        program.pointVertexAttribute(colAttrib, 3, Float.SIZE * 7, Float.SIZE * 2);

        int texAttrib = program.getAttributeLocation("texcoord");
        program.enableVertexAttribute(texAttrib);
        program.pointVertexAttribute(texAttrib, 2, Float.SIZE * 7, Float.SIZE * 5);
    }

    public void begin() {
        if (drawing) {
            throw new RuntimeException("Renderer is already drawing");
        }
        drawing = true;
        numVertices = 0;
    }

    public void end() {
        if (!drawing) {
            throw new RuntimeException("Renderer isn't drawing");
        }
        drawing = false;
        flush();
    }

    public void flush() {
        if (numVertices > 0) {
            vertices.flip();
            if (vao != null) {
                vao.bind();
            } else {
                vbo.bind();
                specifyVertexAttributes();
            }
            program.use();
            vbo.bind();
            vbo.uploadSubData(vertices);

            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, numVertices);

            vertices.clear();
            numVertices = 0;
        }
    }

    public void renderTexture(Vector2f winPos1, Vector2f winPos2, Vector2f)

    public void renderTexture(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2, float r, float g, float b) {
        if (vertices.remaining() < 42) {
            flush();
        }

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1)
                .put(x1).put(y2).put(r).put(g).put(b).put(s1).put(t2)
                .put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2)
                .put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1)
                .put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2)
                .put(x2).put(y1).put(r).put(g).put(b).put(s2).put(t1);

        numVertices += 6;
    }

}
