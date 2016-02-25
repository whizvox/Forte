package whizvox.forte.client.util;

import org.lwjgl.BufferUtils;

import java.awt.Color;
import java.nio.FloatBuffer;

public class GLColor {

    public static final GLColor
            WHITE = new GLColor(0xffffffff),
            BLACK = new GLColor(0x000000ff),
            RED = new GLColor(0xff0000ff),
            GREEN = new GLColor(0x00ff00ff),
            BLUE = new GLColor(0x0000ffff),
            TRANSPARENT = new GLColor(0xffffff00);

    private float red, green, blue, alpha;

    public GLColor(float red, float green, float blue, float alpha) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    public GLColor(float red, float green, float blue) {
        this(red, green, blue, 1f);
    }

    public GLColor(int red, int green, int blue, int alpha) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    public GLColor(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    public GLColor(byte red, byte green, byte blue, byte alpha) {
        setRed(red);
        setGreen(green);
        setBlue(blue);
        setAlpha(alpha);
    }

    public GLColor(byte red, byte green, byte blue) {
        this(red, green, blue, (byte) 0xff);
    }

    public GLColor(ColorFormat format, int color) {
        setColor(format, color);
    }

    public GLColor(int color) {
        this(ColorFormat.RGBA, color);
    }

    public GLColor(Color color) {
        this(color.getRGB());
    }

    public void setColor(ColorFormat format, int color) {
        float[] data = new float[4];
        data[0] = ((color >> 24) & 0xff) / 255f;
        data[1] = ((color >> 16) & 0xff) / 255f;
        data[2] = ((color >> 8) & 0xff) / 255f;
        data[3] = (color & 0xff) / 255f;
        float r, g, b, a;
        switch (format) {
            case RGBA:
                r = data[0];
                g = data[1];
                b = data[2];
                a = data[3];
                break;
            case ARGB:
                r = data[1];
                g = data[2];
                b = data[3];
                a = data[0];
                break;
            case RGB:
                r = data[1];
                g = data[2];
                b = data[3];
                a = 1f;
                break;
            case CMYK:
                float c = data[0];
                float m = data[1];
                float y = data[2];
                float k = data[3];
                r = (1f - c) * (1f - k);
                g = (1f - m) * (1f - k);
                b = (1f - y) * (1f - k);
                a = 1f;
                break;
            default:
                r = 1f;
                g = 1f;
                b = 1f;
                a = 1f;
        }
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    public void setRed(float red) {
        this.red = clampColor(red);
    }

    public void setRed(int red) {
        this.red = clampColor(red) / 255f;
    }

    public void setRed(byte red) {
        this.red = (red & 0xff) / 255f;
    }

    public void setGreen(float green) {
        this.green = clampColor(green);
    }

    public void setGreen(int green) {
        this.green = clampColor(green) / 255f;
    }

    public void setGreen(byte green) {
        this.green = (green & 0xff) / 255f;
    }

    public void setBlue(float blue) {
        this.blue = clampColor(blue);
    }

    public void setBlue(int blue) {
        this.blue = clampColor(blue) / 255f;
    }

    public void setBlue(byte blue) {
        this.blue = (blue & 0xff) / 255f;
    }

    public void setAlpha(float alpha) {
        this.alpha = clampColor(alpha);
    }

    public void setAlpha(int alpha) {
        this.alpha = clampColor(alpha) / 255f;
    }

    public void setAlpha(byte alpha) {
        this.alpha = (alpha & 0xff) / 255f;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public int getColor(ColorFormat format) {
        int color = 0;
        final float[] byteOrder = new float[4];
        switch (format) {
            case RGBA:
                byteOrder[0] = red;
                byteOrder[1] = green;
                byteOrder[2] = blue;
                byteOrder[3] = alpha;
                break;
            case ARGB:
                byteOrder[0] = alpha;
                byteOrder[1] = red;
                byteOrder[2] = green;
                byteOrder[3] = blue;
                break;
            case RGB:
                byteOrder[0] = 1f;
                byteOrder[1] = red;
                byteOrder[2] = green;
                byteOrder[3] = blue;
                break;
            case CMYK:
                float k = 1f - Math.max(red, Math.max(green, blue));
                float c = (1f - red - k) / (1f - k);
                float m = (1f - green - k) / (1f - k);
                float y = (1f - blue - k) / (1f - k);
                byteOrder[0] = c;
                byteOrder[1] = m;
                byteOrder[2] = y;
                byteOrder[3] = k;
                break;
        }
        color |= (int) (byteOrder[0] * 255) << 24;
        color |= (int) (byteOrder[1] * 255) << 16;
        color |= (int) (byteOrder[2] * 255) << 8;
        color |= (int) (byteOrder[3] * 255);
        return color;
    }

    public int getColor() {
        return getColor(ColorFormat.RGBA);
    }

    public void setBrightness(float amount) {
        setRed(red * amount);
        setGreen(green * amount);
        setBlue(blue * amount);
    }

    public void tweakBrightness(float factor) {
        setRed(red + factor);
        setGreen(green + factor);
        setBlue(blue + factor);
    }

    public void setOpacity(float amount) {
        setAlpha(alpha * amount);
    }

    public void tweakOpacity(float amount) {
        setAlpha(alpha + amount);
    }

    public FloatBuffer createBuffer() {
        FloatBuffer buf = BufferUtils.createFloatBuffer(4);
        return fillBuffer(buf);
    }

    public FloatBuffer fillBuffer(FloatBuffer buf) {
        if (buf.remaining() == 4) {
            buf.put(red).put(green).put(blue).put(alpha);
        }
        return buf;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof GLColor) {
            GLColor other = (GLColor) obj;
            return other.red == red && other.green == green && other.blue == blue && other.alpha == alpha;
        }
        return false;
    }

    public static GLColor fromOther(ColorFormat format, int color) {
        GLColor base = new GLColor(0);
        base.setColor(format, color);
        return base;
    }

    private static float clampColor(float c) {
        if (c > 1f) return 1f;
        if (c < 0f) return 0f;
        return c;
    }

    private static int clampColor(int c) {
        if (c > 255) return 255;
        if (c < 0) return 0;
        return c;
    }

    public enum ColorFormat {
        RGBA,
        ARGB,
        RGB,
        CMYK
    }

}
