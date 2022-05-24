package com.thana.hwapp.utils;

// This class designed by: HTMLChannel
public class TimingUtil {

    public static boolean mod(int c) {
        return System.currentTimeMillis() % c == 0;
    }
}
