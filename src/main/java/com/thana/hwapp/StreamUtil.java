package com.thana.hwapp;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

    public static String read(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        int c;
        while ((c = stream.read()) != -1) {
            builder.append((char) c);
        }
        return builder.toString();
    }
}
