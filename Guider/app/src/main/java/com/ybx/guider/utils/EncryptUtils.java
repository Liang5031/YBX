package com.ybx.guider.utils;

//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class EncryptUtils {

    /// <summary>
    /// 3des解码
    /// </summary>
    /// <param name="value">待解密字符串</param>
    /// <param name="key">原始密钥字符串</param>
    /// <returns></returns>
    public static String Decrypt3DES(String value, String key) throws Exception {
        byte[] b = decryptMode(GetKeyBytes(key), Base64.decode(value, Base64.DEFAULT));
        return new String(b);
    }

    /// <summary>
    /// 3des加密
    /// </summary>
    /// <param name="value">待加密字符串</param>
    /// <param name="strKey">原始密钥字符串</param>
    /// <returns></returns>
    public static String Encrypt3DES(String value, String key) throws Exception {
        if (key.length() < 24)
        {
            key+="000000000000000000000000000000000";
        }
        key = key.substring(0, 24);

//        String str = byte2Base64(encryptMode(GetKeyBytes(key), value.getBytes()));
        String str = byte2Base64(encryptMode(key.getBytes("UTF-8"), value.getBytes("UTF-8")));
        return str;
    }

    //计算24位长的密码byte值,首先对原始密钥做MD5算hash值，再用前8位数据对应补全后8位
    public static byte[] GetKeyBytes(String strKey) throws Exception {
        if (null == strKey || strKey.length() < 1) {
            throw new Exception("key is null or empty!");
        }

        MessageDigest alg = MessageDigest.getInstance("MD5");
        alg.update(strKey.getBytes());
        byte[] bkey = alg.digest();

        int start = bkey.length;
        byte[] bkey24 = new byte[24];
        for (int i = 0; i < start; i++) {
            bkey24[i] = bkey[i];
        }

        //为了与.net16位key兼容
        for (int i = start; i < 24; i++) {
            bkey24[i] = bkey[i - start];
        }

        return bkey24;
    }


    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish


    //keybyte为加密密钥，长度为24字节
    //src为被加密的数据缓冲区（源）
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {

        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); //加密
            Cipher c1 = Cipher.getInstance(Algorithm);

            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }


    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {

        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }

        return null;
    }


    //转换成base64编码
    public static String byte2Base64(byte[] b) {
//        return Base64.encodeToString(b, Base64.DEFAULT);
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }


    //转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;

            if (n < b.length - 1)
                hs = hs + ":";
        }

        return hs.toUpperCase();
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String generateSign(String paramsInOrder, String password) {
//        SimpleDateFormat s = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
//        String timestamp = s.format(new Date());
        String timestamp = "201603040052440967";

//        String hashPWD = md5(password).toUpperCase();
        String d1 = md5(paramsInOrder.toUpperCase());

        String p1 = md5(password).toUpperCase();
        String d2 = md5(p1.toUpperCase());
        String d3 = d1 + timestamp + d2;

        String d4 = null;
        try {
            d4 = Encrypt3DES(d3.toUpperCase(), p1.toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return d4;
    }
}