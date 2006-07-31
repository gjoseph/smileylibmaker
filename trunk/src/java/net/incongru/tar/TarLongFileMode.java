/**
 * Created by IntelliJ IDEA.
 * User: greg
 * Date: Oct 9, 2004
 * Time: 7:50:32 PM
 * To change this template use File | Settings | File Templates.
 */
package net.incongru.tar;

public class TarLongFileMode {
    public static final TarLongFileMode WARN = new TarLongFileMode("WARN");
    public static final TarLongFileMode FAIL = new TarLongFileMode("FAIL");
    public static final TarLongFileMode TRUNCATE = new TarLongFileMode("TRUNCATE");
    public static final TarLongFileMode GNU = new TarLongFileMode("GNU");
    public static final TarLongFileMode OMIT = new TarLongFileMode("OMIT");

    private final String name;

    private TarLongFileMode(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TarLongFileMode)) return false;

        final TarLongFileMode tarLongFileMode = (TarLongFileMode) o;

        if (!name.equals(tarLongFileMode.name)) return false;

        return true;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
