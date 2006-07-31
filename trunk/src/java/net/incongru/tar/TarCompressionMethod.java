/**
 * Created by IntelliJ IDEA.
 * User: greg
 * Date: Oct 9, 2004
 * Time: 7:52:54 PM
 * To change this template use File | Settings | File Templates.
 */
package net.incongru.tar;

import org.apache.tools.bzip2.CBZip2OutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class TarCompressionMethod {
    public static final TarCompressionMethod NONE = new TarCompressionMethod("NONE");
    public static final TarCompressionMethod GZIP = new TarCompressionMethod("GZIP");
    public static final TarCompressionMethod BZIP2 = new TarCompressionMethod("BZIP2");

    private final String name;

    private TarCompressionMethod(String name) {
        this.name = name;
    }

    /**
     * This method wraps the output stream with the
     * corresponding compression method
     *
     * @param ostream output stream
     * @return output stream with on-the-fly compression
     * @throws java.io.IOException thrown if file is not writable
     */
    public OutputStream compress(final OutputStream ostream) throws IOException {
        if (GZIP.equals(this)) {
            return new GZIPOutputStream(ostream);
        } else {
            if (BZIP2.equals(this)) {
                ostream.write('B');
                ostream.write('Z');
                return new CBZip2OutputStream(ostream);
            }
        }
        return ostream;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TarCompressionMethod)) return false;

        final TarCompressionMethod tarCompressionMethod = (TarCompressionMethod) o;

        if (!name.equals(tarCompressionMethod.name)) return false;

        return true;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
