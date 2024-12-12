package com.hdbsthor012airflow.util;

import java.util.UUID;

//下面就是实现为数据库获取一个唯一的主键id的代码 
public class UUIDGenerator {
    public UUIDGenerator() {
    }

    /**
     * 获得一个UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        String s = UUID.randomUUID().toString();
        //去掉“-”符号 
        return s.replace("-", "");
    }

    /**
     * 获得指定数目的UUID
     *
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }

    public static String shortId(String ID, int urlLength) {
        if (urlLength < 4 ) {
            urlLength = 8;// defalut length
        }
        StringBuilder sbBuilder = new StringBuilder(urlLength + 2);
        String md5Hex = "";
        int nLen = 0;
        while (nLen < urlLength) {
            // 这个方法是先 md5 再 base64编码 参见
            // https://github.com/ndxt/centit-commons/blob/master/centit-utils/src/main/java/com/centit/support/security/Md5Encoder.java
            md5Hex = Md5Encoder.encodeBase64(md5Hex + ID);
            for(int i=0;i<md5Hex.length();i++){
                char c = md5Hex.charAt(i);
                if(c != '/' && c != '+'){
                    sbBuilder.append(c);
                    nLen ++;
                }
                if(nLen == urlLength){
                    break;
                }
            }
        }
        return sbBuilder.toString().toUpperCase();
    }

}   