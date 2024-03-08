package com.cpy.utils;

/**
 * 校验工具类
 */
public class VerifyUtils {
    /**
     *
     * @param string
     * @return true 为校验正确，false为校验错误
     */
    public static boolean verifyString(String string){
        if(string==null||string.equals("")){
            return false;
        }
        return true;
    }

}
