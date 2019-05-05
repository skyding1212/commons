package com.alex.commons.util.secury;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * TODO base64加解密
 */
public class Base64Utils {

    /**
     * TODO 加密
     *
     * @param b
     * @return
     * @throws UnsupportedEncodingException
     * @author du
     * @version 2016年5月23日 下午4:24:19
     */
    public static String encode(byte[] b) throws UnsupportedEncodingException {
        String s = null;
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    /**
     * TODO 加密
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     * @author du
     * @version 2016年5月23日 下午4:24:19
     */
    public static String encodeBase64(String str) throws UnsupportedEncodingException {
        byte[] b = null;
        String s = null;
        b = str.getBytes("utf-8");
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    /**
     * TODO 解密
     *
     * @param s
     * @return
     * @throws IOException
     * @author du
     * @version 2016年5月23日 下午4:24:52
     */
    public static byte[] decode(String s) throws IOException {
        byte[] b = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            b = decoder.decodeBuffer(s);
        }
        return b;
    }

    /**
     * TODO 解密
     *
     * @param s
     * @return
     * @throws IOException
     * @version 2016年5月23日 下午4:24:52
     */
    public static String decodeBase64(String s) throws IOException {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            b = decoder.decodeBuffer(s);
            result = new String(b, "utf-8");
        }
        return result;
    }
}
