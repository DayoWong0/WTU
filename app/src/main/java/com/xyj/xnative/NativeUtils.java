package com.xyj.xnative;

public class NativeUtils {
    static {
        System.loadLibrary("sign");
    }

    public static native String sign(String str);
    public static native String encode(String str);
    public static native String decode(String str);

}