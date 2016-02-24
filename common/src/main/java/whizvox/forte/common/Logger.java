package whizvox.forte.common;

import java.io.OutputStream;
import java.io.PrintStream;

public class Logger extends PrintStream {

    public final String name, format_plain = "%s [%s] %s", format_level = "%s [%s] %s: %s";
    public final PrintStream file_out;

    public Logger(OutputStream out, String name, PrintStream file_out) {
        super(out);
        this.name = name;
        this.file_out = file_out;
    }

    private String getPlainMessage(String msg) {
        return String.format(format_plain, StringUtils.getCompactTimestamp(), name, msg);
    }

    private String getLoggingMessage(Level level, String msg) {
        return String.format(format_level, StringUtils.getCompactTimestamp(), name, level.toString(), msg);
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        file_out.write(buf, off, len);
    }

    @Override
    public void println(String x) {
        super.println(getPlainMessage(x));
    }

    @Override
    public void println(boolean x) {
        super.println(getPlainMessage(Boolean.toString(x)));
    }

    @Override
    public void println(char x) {
        super.println(getPlainMessage(Character.toString(x)));
    }

    @Override
    public void println(int x) {
        super.println(getPlainMessage(Integer.toString(x)));
    }

    @Override
    public void println(long x) {
        super.println(getPlainMessage(Long.toString(x)));
    }

    @Override
    public void println(float x) {
        super.println(getPlainMessage(Float.toString(x)));
    }

    @Override
    public void println(double x) {
        super.println(getPlainMessage(Double.toString(x)));
    }

    @Override
    public void println(char[] x) {
        super.println(getPlainMessage(new String(x)));
    }

    @Override
    public void println(Object x) {
        super.println(getPlainMessage(String.valueOf(x)));
    }

    @Override
    public void flush() {
        super.flush();
        file_out.flush();
    }

    @Override
    public void close() {
        super.close();
        file_out.close();
    }

    public void info(String msg) {
        super.println(getLoggingMessage(Level.INFO, msg));
    }

    public void infof(String format, Object... args) {
        info(String.format(format, args));
    }

    public void debug(String msg) {
        super.println(getLoggingMessage(Level.DEBUG, msg));
    }

    public void debugf(String format, Object... args) {
        debug(String.format(format, args));
    }

    public void warn(String msg) {
        super.println(getLoggingMessage(Level.WARNING, msg));
    }

    public void warnf(String format, Object... args) {
        warn(String.format(format, args));
    }

    public void severe(String msg) {
        super.println(getLoggingMessage(Level.SEVERE, msg));
    }

    public void severef(String format, Object... msg) {
        severe(String.format(format, msg));
    }

    public void stacktrace(Throwable t) {
        super.println(getLoggingMessage(Level.ERROR, t.getMessage()));
        for (StackTraceElement element : t.getStackTrace()) {
            super.println('\t' + element.toString());
        }
    }

    private enum Level {
        INFO,
        DEBUG,
        WARNING,
        SEVERE,
        ERROR
    }

}
