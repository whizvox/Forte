package whizvox.forte.client.render;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {

    public static final int
        TARGET_TEXTURE_2D = GL11.GL_TEXTURE_2D,
        BYTES_PER_PIXEL = 4;

    private int id, width, height;

    public Texture(int width, int height, ByteBuffer data) {
        id = GL11.glGenTextures();
        this.width = width;
        this.height = height;

        bind();
        GL11.glTexParameteri(TARGET_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(TARGET_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(TARGET_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
        GLUtil.unbindTexture();
    }

    public void bind() {
        GL11.glBindTexture(TARGET_TEXTURE_2D, id);
    }

    public void delete() {
        GL11.glDeleteTextures(id);
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static Texture load(String filePath) {
        IntBuffer
                widthBuf = BufferUtils.createIntBuffer(1),
                heightBuf = BufferUtils.createIntBuffer(1),
                compBuf = BufferUtils.createIntBuffer(1);

        STBImage.stbi_set_flip_vertically_on_load(GL11.GL_TRUE);
        ByteBuffer data = STBImage.stbi_load(filePath, widthBuf, heightBuf, compBuf, BYTES_PER_PIXEL);
        if (data == null) {
            throw new RuntimeException(STBImage.stbi_failure_reason());
        }
        return new Texture(widthBuf.get(), heightBuf.get(), data);
    }

    public static Texture load(PNGDecoder decoder) {
        try {
            int width = decoder.getWidth();
            int height = decoder.getHeight();
            ByteBuffer data = BufferUtils.createByteBuffer(width * height * BYTES_PER_PIXEL);
            decoder.decode(data, width, PNGDecoder.Format.RGBA);
            data.flip();
            return new Texture(width, height, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
