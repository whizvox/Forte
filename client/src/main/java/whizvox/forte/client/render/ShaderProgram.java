package whizvox.forte.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import whizvox.forte.client.math.*;

import java.nio.FloatBuffer;

public class ShaderProgram {

    private int id;

    public ShaderProgram() {
        id = GL20.glCreateProgram();
    }

    public void attachShader(Shader shader) {
        GL20.glAttachShader(id, shader.getId());
    }

    public void bindFragmentData(int number, CharSequence name) {
        GL30.glBindFragDataLocation(id, number, name);
    }

    public void link() {
        GL20.glLinkProgram(id);
        if (GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetProgramInfoLog(id));
        }
    }

    public void use() {
        GL20.glUseProgram(id);
    }

    public void delete() {
        GL20.glDeleteProgram(id);
    }

    public int getAttributeLocation(CharSequence name) {
        return GL20.glGetAttribLocation(id, name);
    }

    public void enableVertexAttribute(int location) {
        GL20.glEnableVertexAttribArray(location);
    }

    public void disableVertexAttribute(int location) {
        GL20.glDisableVertexAttribArray(location);
    }

    public void pointVertexAttribute(int location, int size, int stride, int offset) {
        GL20.glVertexAttribPointer(location, size, GL11.GL_FLOAT, false, stride, offset);
    }

    public int getUniformLocation(CharSequence name) {
        return GL20.glGetUniformLocation(id, name);
    }

    public void setUniform1i(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    public void setUniform2f(int location, FloatBuffer value) {
        GL20.glUniform2fv(location, value);
    }

    public void setUniform2f(int location, Vector2f value) {
        setUniform2f(location, value.getBuffer());
    }

    public void setUniform3f(int location, FloatBuffer value) {
        GL20.glUniform3fv(location, value);
    }

    public void setUniform3f(int location, Vector3f value) {
        setUniform3f(location, value.getBuffer());
    }

    public void setUniform4f(int location, FloatBuffer value) {
        GL20.glUniform4fv(location, value);
    }

    public void setUniform4f(int location, Vector4f value) {
        setUniform4f(location, value.getBuffer());
    }

    public void setUniformMatrix2f(int location, FloatBuffer value) {
        GL20.glUniformMatrix2fv(location, false, value);
    }

    public void setUniformMatrix2f(int location, Matrix2f value) {
        setUniformMatrix2f(location, value.getBuffer());
    }

    public void setUniformMatrix3f(int location, FloatBuffer value) {
        GL20.glUniformMatrix3fv(location, false, value);
    }

    public void setUniformMatrix3f(int location, Matrix3f value) {
        setUniformMatrix3f(location, value.getBuffer());
    }

    public void setUniformMatrix4f(int location, FloatBuffer value) {
        GL20.glUniformMatrix2fv(location, false, value);
    }

    public void setUniformMatrix4f(int location, Matrix4f value) {
        setUniformMatrix4f(location, value.getBuffer());
    }

}
