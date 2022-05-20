package com.boyuan.netty.com.boyuan.netty.hexstring;

import com.boyuan.netty.util.CommUtil;
import org.apache.commons.lang3.RandomStringUtils;

public class D3000ALoginHexString implements MakeHexString{

    @Override
    public String getHeader(){
        String header = "55AA";
        return header;
    }

    @Override
    public String getDeviceID() {
        String s = RandomStringUtils.randomNumeric(18);

        String DeviceID = "E"+s;
        System.out.printf(DeviceID+"\r\n");
        return DeviceID;
    }

    @Override
    public String getBody() {
        String userNamePassword = "64 65 76 5F 30 30 30 30 31 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 70 61 73 73 77 6F 72 64 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        String s = "02"+"00"+"0000"+ CommUtil.str2HexStr(getDeviceID())+userNamePassword.replaceAll(" ","");
        return s;
    }
    @Override
    public String getLen(){

        return "";
    }

    public static void main(String[] args) {
        D3000ALoginHexString hx = new D3000ALoginHexString();
        String s = hx.getBody();
        //String tes = "02 00 00 00 45 36 33 30 31 32 30 31 38 30 35 31 30 31 35 34 33 33 32 64 65 76 5F 30 30 30 30 31 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 70 61 73 73 77 6F 72 64 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";
        byte[] bytes = CommUtil.hexStringToByteArray(s);
        String len =Integer.toHexString(bytes.length+2);
        String SS = "00"+len+s;
        System.out.printf(SS+"\r\n");

        byte[] b = CommUtil.hexStringToByteArray(SS);
        int a = 0 ;
        for(int i = 0 ;i<=b.length-1;i++){
            a^=b[i];
        }

        String crchex =Integer.toHexString(a);
        String crcs = SS+"00"+crchex;
        String D3000LogHexString = hx.getHeader()+crcs;
        System.out.printf(D3000LogHexString+"\n\r");

        String ss = "f0 51 8b 41";
        byte[] bb = CommUtil.hexStringToByteArray(ss);
        System.out.printf(CommUtil.byteArrayToFloatList(bb).toString());

    }

}
