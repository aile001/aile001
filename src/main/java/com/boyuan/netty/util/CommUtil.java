package com.boyuan.netty.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CommUtil {


    //CRC-16/XMODEM校验
    public static String GetCRC_XMODEM_Str(String str) {
        byte[] bytes = hexStringToByteArray(str);//16进制字符串转成16进制字符串数组
        int i = CRC16_XMODEM(bytes);//进行CRC—XMODEM校验得到十进制校验数
        String CRC = Integer.toHexString(i);//10进制转16进制
        return CRC;
    }

    public static int CRC16_XMODEM(byte[] buffer) {
        /* StringUtil.getByteArrayByString();*/

        int wCRCin = 0x0000; // initial value 65535
        int wCPoly = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)
        for (byte b : buffer) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((wCRCin >> 15 & 1) == 1);
                wCRCin <<= 1;
                if (c15 ^ bit)
                    wCRCin ^= wCPoly;
            }
        }
        wCRCin &= 0xffff;
        return wCRCin ^= 0x0000;
    }
    //byte数组转换为十六进制的字符串
    public static String conver16HexStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            if ((b[i] & 0xff) < 0x10)
                result.append("0");
            result.append(Long.toString(b[i] & 0xff, 16));
        }
        return result.toString().toUpperCase();
    }

    //十六进制的字符串转byte数组  hexstring to bytes[]
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    //CRC16  校验和  /** 计算校验位 ，返回十六进制校验位 */
    public static String makeCheckSum(String data) {
        int dSum = 0;
        int length = data.length();
        int index = 0;
        // 遍历十六进制，并计算总和
        while (index < length) {
            String s = data.substring(index, index + 2); // 截取2位字符

            dSum += Integer.parseInt(s, 16); // 十六进制转成十进制 , 并计算十进制的总和

            index = index + 2;
        }

        //int mod = dSum % 256; // 用256取余，十六进制最大是FF，FF的十进制是255

        //String checkSumHex = Integer.toHexString(mod); // 余数转成十六进制
        String checkSumHex = Integer.toHexString(dSum);  //十进制校验和转16进制
        length = checkSumHex.length();
        if (length < 8) {
            checkSumHex = "0" + checkSumHex;  // 校验位不足两位的，在前面补0
        }
        System.out.println("chesum:"+checkSumHex);
        return checkSumHex;
    }

    /**
     * 将byte类型的arr转换成double
     * @param arr
     * @return
     */
    public static List<Double> byteArrayToDoubleList(byte[] arr){
        List<Double> d = new ArrayList<>(arr.length/8);
        byte[] doubleBuffer = new byte[8];
        for(int j = 0; j < arr.length; j += 8) {
            System.arraycopy(arr, j, doubleBuffer, 0, doubleBuffer.length);
            d.add(bytes2Double(doubleBuffer));
        }
        return d;
    }
    /**
     * 将byte转换成double
     * @param arr
     * @return
     */
    public static double bytes2Double(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8* i);
        }
        return Double.longBitsToDouble(value);
    }
    /**
     * 将byte类型的arr转换成float
     * @return
     */
    public static List<Float> byteArrayToFloatList(byte[] bytes){
        List<Float> d = new ArrayList<>(bytes.length/8);
        byte[] doubleBuffer = new byte[4];
        for(int j = 0; j < bytes.length; j += 4) {
            System.arraycopy(bytes, j, doubleBuffer, 0, doubleBuffer.length);
            d.add(bytes2Float(doubleBuffer));
        }
        return d;
    }


    /**
     * 将byte数组数据转换成float
     * @param arr
     * @return
     */
    public static float bytes2Float(byte[] arr) {
        int accum = 0;
        accum = accum|(arr[0] & 0xff) << 0;
        accum = accum|(arr[1] & 0xff) << 8;
        accum = accum|(arr[2] & 0xff) << 16;
        accum = accum|(arr[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }

    public static char[] byteToChars(byte[] bytes) {
        Charset charset = Charset.forName("ISO-8859-1");
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        CharBuffer charBuffer = charset.decode(byteBuffer);
        return charBuffer.array();
    }


    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    //hexstring to byte[]
    public static byte[] hexStringToByteArrays(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        System.out.println(len);
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }


    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            //sb.append(' ');
        }
        return sb.toString().trim();
    }

}
