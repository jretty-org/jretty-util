package org.jretty.util.support;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author zollty
 * @since 2020年7月22日
 */
public class ClosedInputStream extends InputStream {

    /**
     * Returns -1 to indicate that the stream is closed.
     *
     * @return always -1
     */
    @Override
    public int read() throws IOException {
        return -1;
    }

}
