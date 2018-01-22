package cn.jxnu.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;

public class MD5Util {
    public static String md5(String str,String salt){
        return new Md5Hash(str,salt).toString() ;
    }

    public static void main(String[] args) {
        String md5 = md5("abc123","crossoverjie") ;
        System.out.println(md5);
    }
}
