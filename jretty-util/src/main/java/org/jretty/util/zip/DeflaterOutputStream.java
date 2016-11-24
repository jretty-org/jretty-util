/* 
 * Copyright (C) 2013-2015 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * Create by ZollTy on 2013-6-07 (http://blog.zollty.com/, zollty@163.com)
 */
/*===========================================================================
 * Licensed Materials - Property of IBM
 * "Restricted Materials of IBM"
 * 
 * IBM SDK, Java(tm) Technology Edition, v6
 * (C) Copyright IBM Corp. 1999, 2005. All Rights Reserved
 *
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 *===========================================================================
 */
/*
 * @(#)DeflaterOutputStream.java	1.36 06/03/13
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.jretty.util.zip;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.zip.Deflater;

/**
 * This class implements an output stream filter for compressing data in
 * the "deflate" compression format. It is also used as the basis for other
 * types of compression filters, such as GZIPOutputStream.
 *
 * @see		Deflater
 * @version 	1.36, 03/13/06
 * @author 	David Connelly
 */
public class DeflaterOutputStream extends FilterOutputStream {
    /**
     * Compressor for this stream.
     */
    protected Deflater def;

    /**
     * Output buffer for writing compressed data.
     */
    protected byte[] buf;
   
    /**
     * Indicates that the stream has been closed.
     */

    private boolean closed = false;

    /**
     * Creates a new output stream with the specified compressor and
     * buffer size.
     * @param out the output stream
     * @param def the compressor ("deflater")
     * @param size the output buffer size
     * @exception IllegalArgumentException if size is <= 0
     */
    public DeflaterOutputStream(OutputStream out, Deflater def, int size) {
        super(out);
        if (out == null || def == null) {
            throw new NullPointerException();
        } else if (size <= 0) {
            throw new IllegalArgumentException("buffer size <= 0");
        }
        this.def = def;
        buf = new byte[size];
    }

    /**
     * Creates a new output stream with the specified compressor and
     * a default buffer size.
     * @param out the output stream
     * @param def the compressor ("deflater")
     */
    public DeflaterOutputStream(OutputStream out, Deflater def) {
        this(out, def, 512);
    }

    boolean usesDefaultDeflater = false;

    /**
     * Creates a new output stream with a default compressor and buffer size.
     * @param out the output stream
     */
    public DeflaterOutputStream(OutputStream out) {
        this(out, new Deflater());
        usesDefaultDeflater = true;
    }

    /**
     * Writes a byte to the compressed output stream. This method will
     * block until the byte can be written.
     * @param b the byte to be written
     * @exception IOException if an I/O error has occurred
     */
    public void write(int b) throws IOException {
        byte[] buf = new byte[1];
        buf[0] = (byte) (b & 0xff);
        write(buf, 0, 1);
    }

    /**
     * Writes an array of bytes to the compressed output stream. This
     * method will block until all the bytes are written.
     * @param b the data to be written
     * @param off the start offset of the data
     * @param len the length of the data
     * @exception IOException if an I/O error has occurred
     */
    public void write(byte[] b, int off, int len) throws IOException {
        if (def.finished()) {
            throw new IOException("write beyond end of stream");
        }
        if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        if (!def.finished()) {
            // Deflate no more than stride bytes at a time. This avoids
            // excess copying in deflateBytes (see Deflater.c)
            int stride = buf.length;
            for (int i = 0; i < len; i += stride) {
                def.setInput(b, off + i, Math.min(stride, len - i));
                while (!def.needsInput()) {
                    deflate();
                }
            }
        }
    }

    /**
     * Finishes writing compressed data to the output stream without closing
     * the underlying stream. Use this method when applying multiple filters
     * in succession to the same output stream.
     * @exception IOException if an I/O error has occurred
     */
    public void finish() throws IOException {
        if (!def.finished()) {
            def.finish();
            while (!def.finished()) {
                deflate();
            }
        }
    }

    /**
     * Writes remaining compressed data to the output stream and closes the
     * underlying stream.
     * @exception IOException if an I/O error has occurred
     */
    public void close() throws IOException {
        if (!closed) {
            finish();
            if (usesDefaultDeflater)
                def.end();
            out.close();
            closed = true;
        }
    }

    /**
     * Writes next block of compressed data to the output stream.
     * @throws IOException if an I/O error has occurred
     */
    protected void deflate() throws IOException {
        int len = def.deflate(buf, 0, buf.length);
        if (len > 0) {
            out.write(buf, 0, len);
        }
    }
}
