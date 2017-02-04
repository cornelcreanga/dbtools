package com.ccreanga.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class IOUtil {


    public static void closeSilent(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {/**ignore**/}
    }

    public static void copy(Reader reader,OutputStream writer) throws IOException{
        char[] buffer=new char[8*1024];
        int len;
        while ( ( len=reader.read(buffer) ) >= 0 ) {
            writer.write(new String(buffer,0,len).getBytes(StandardCharsets.UTF_8));
        }
    }

    public static long copy(InputStream from, OutputStream to, int bufferSize) throws IOException {
        return copy(from, to, -1, -1, bufferSize, null);
    }


    public static long copy(InputStream from, OutputStream to) throws IOException {
        return copy(from, to, -1, -1);
    }

    public static long copy(InputStream from, OutputStream to, long inputOffset, long length) throws IOException {
        return copy(from, to, inputOffset, length, 8 * 1024, null);
    }

    public static long copy(InputStream from, OutputStream to, long inputOffset, long length, int bufferSize, MessageDigest md) throws IOException {

        byte[] buffer = new byte[bufferSize];

        if (inputOffset > 0) {
            long skipped = from.skip(inputOffset);
            if (skipped != inputOffset) {
                throw new EOFException("Bytes to skip: " + inputOffset + " actual: " + skipped);
            }

        }
        if (length == 0) {
            return 0;
        }
        final int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < bufferLength) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && -1 != (read = from.read(buffer, 0, bytesToRead))) {
            to.write(buffer, 0, read);
            if (md != null)
                md.update(buffer, 0, read);
            totalRead += read;
            if (length > 0) {
                bytesToRead = (int) Math.min(length - totalRead, bufferLength);
            }
        }
        return totalRead;
    }

}
