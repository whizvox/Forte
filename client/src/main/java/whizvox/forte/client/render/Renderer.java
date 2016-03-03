package whizvox.forte.client.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import whizvox.forte.client.core.ForteClient;
import whizvox.forte.client.math.Matrix4f;
import whizvox.forte.client.math.Vector2f;
import whizvox.forte.client.util.GLColor;
import whizvox.forte.common.IOUtils;

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

    public Renderer init(int maxVertices) {
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
            shader_vert = Shader.loadVertex(IOUtils.getInternalResource("res/default_vertex.glsl"));
            shader_frag = Shader.loadFragment(IOUtils.getInternalResource("res/default_fragment.glsl"));
        } else {
            shader_vert = Shader.loadVertex(IOUtils.getInternalResource("res/legacy_vertex.glsl"));
            shader_frag = Shader.loadFragment(IOUtils.getInternalResource("res/legacy_fragment.glsl"));
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

        int uniTex = program.getUniformLocation("u_texture");
        program.setUniform1i(uniTex, 0);

        Matrix4f modelMatrix = new Matrix4f();
        int uniModel = program.getUniformLocation("u_model");
        program.setUniformMatrix4f(uniModel, modelMatrix);

        Matrix4f viewMatrix = new Matrix4f();
        int uniView = program.getUniformLocation("u_view");
        program.setUniformMatrix4f(uniView, viewMatrix);

        Matrix4f projMatrix = Matrix4f.orthographic(0f, width, 0f, height, -1f, 1f);
        int uniProj = program.getUniformLocation("u_projection");
        program.setUniformMatrix4f(uniProj, projMatrix);
        return this;
    }

    protected void specifyVertexAttributes() {
        int posAttrib = program.getAttributeLocation("v_vertPos");
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 2, Float.SIZE * 7, 0);

        int colAttrib = program.getAttributeLocation("v_color");
        program.enableVertexAttribute(colAttrib);
        program.pointVertexAttribute(colAttrib, 3, Float.SIZE * 7, Float.SIZE * 2);

        int texAttrib = program.getAttributeLocation("v_texPos");
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

    public void renderTexture(Texture texture, float x, float y) {
        renderTextureRegion(texture, x, y, 0, 0, texture.getWidth(), texture.getHeight(), GLColor.WHITE);
    }

    public void renderTexture(Texture texture, float x, float y, GLColor color) {
        renderTextureRegion(texture, x, y, 0, 0, texture.getWidth(), texture.getHeight(), color);
    }

    public void renderTextureRegion(Texture texture, float x, float y, int regX, int regY, int regWidth, int regHeight) {
        renderTextureRegion(texture, x, y, regX, regY, regWidth, regHeight, GLColor.WHITE);
    }

    public void renderTextureRegion(Texture texture, float x, float y, int regX, int regY, int regWidth, int regHeight, GLColor color) {
        float x2 = x + texture.getWidth();
        float y2 = y + texture.getHeight();
        float s1 = (float) regX / texture.getWidth();
        float t1 = (float) regY / texture.getHeight();
        float s2 = (float) (regX + regWidth) / texture.getWidth();
        float t2 = (float) (regY + regHeight) / texture.getHeight();
        renderTextureRegion(x, y, x2, y2, s1, t1, s2, t2, color);
    }

    public void renderTextureRegion(Vector2f winPos1, Vector2f winPos2, Vector2f texPos1, Vector2f texPos2, GLColor color) {
        renderTextureRegion(winPos1.x, winPos1.y, winPos2.x, winPos2.y, texPos1.x, texPos1.y, texPos2.x, texPos2.y, color);
    }

    public void renderTextureRegion(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2, GLColor color) {
        if (vertices.remaining() < 42) {
            flush();
        }

        float r = color.getRed();
        float g = color.getGreen();
        float b = color.getBlue();

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1)
                .put(x1).put(y2).put(r).put(g).put(b).put(s1).put(t2)
                .put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2)
                .put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1)
                .put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2)
                .put(x2).put(y1).put(r).put(g).put(b).put(s2).put(t1);

        numVertices += 6;
    }

}
