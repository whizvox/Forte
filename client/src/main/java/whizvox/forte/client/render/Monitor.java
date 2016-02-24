package whizvox.forte.client.render;

public class Monitor {

    public final long handle;
    public final int width, height, refreshRate;
    public final String name;

    public Monitor(long handle, int width, int height, int refreshRate, String name) {
        this.handle = handle;
        this.width = width;
        this.height = height;
        this.refreshRate = refreshRate;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Monitor && ((Monitor) obj).handle == handle;
    }

}
