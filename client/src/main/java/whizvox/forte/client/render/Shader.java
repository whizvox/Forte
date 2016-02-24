package whizvox.forte.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shader {

    public static final int
        TYPE_VERTEX = GL20.GL_VERTEX_SHADER,
        TYPE_FRAGMENT = GL20.GL_FRAGMENT_SHADER;

    private int id;

    public Shader(int type, CharSequence source) {
        id = GL20.glCreateShader(type);
        GL20.glShaderSource(id, source);
        GL20.glCompileShader(id);
        if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) != GL11.GL_TRUE) {
            throw new RuntimeException(GL20.glGetShaderInfoLog(id));
        }
    }

    public void delete() {
        GL20.glDeleteShader(id);
    }

    public int getId() {
        return id;
    }

    public static Shader load(InputStream in, int type) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return new Shader(type, sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Shader loadVertex(InputStream in) {
        return load(in, TYPE_VERTEX);
    }

    public static Shader loadFragment(InputStream in) {
        return load(in, TYPE_FRAGMENT);
    }

}
