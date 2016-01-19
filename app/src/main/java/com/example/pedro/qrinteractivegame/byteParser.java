package com.example.pedro.qrinteractivegame;

/**
 * Created by Pedro on 17/01/2016.
 */
public class byteParser {
    public static byte[] qr_to_byte(String qr){
        String str=new String(qr);
        str=str.replaceAll("http://","");
        str=str.replaceAll("www.","");
        str=str.replaceAll("/","");
        while (str.length()<27){
            str+=str;
        }
        byte[] qr_code=str.getBytes();
        byte[] final_code=new byte [qr_code.length+5];
        int l=final_code.length;

        for (int i=1; i<=5; i++) {
            final_code[l - i] = 0x00;
        }
        for (int i=0;i<qr_code.length;i++){
            final_code[i]=qr_code[i];
        }
        return final_code;
    }


}
