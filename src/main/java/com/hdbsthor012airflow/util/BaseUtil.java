package com.hdbsthor012airflow.util;

import java.util.*;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Created by songyouming on 2017/7/26.
 */
public class BaseUtil {

    public static boolean Base_HasValue(Object pValue) {
        Boolean HasValue = true;
        if (pValue == (null)) {
            HasValue = false;
        }
        return HasValue;
    }

    public static boolean Base_HasValue(String pValue) {
        Boolean HasValue = true;
        if (pValue == (null)) {
            HasValue = false;
        } else if (pValue.equals("")) {
            HasValue = false;
        } else if (pValue.trim().equals("")) {
            HasValue = false;
        } else if (pValue.trim().equals("null")) {
            HasValue = false;
        } else if (pValue.trim().equals("undefined")) {
            HasValue = false;
        }
        return HasValue;
    }

    public static boolean Base_HasValue(Integer pValue) {
        Boolean HasValue = true;
        if (pValue == (null)) {
            HasValue = false;
        }
        return HasValue;
    }

    public static boolean Base_HasValue(Map pValue) {
        Boolean HasValue = true;
        if (pValue == (null) || pValue.size() < 1) {
            HasValue = false;
        }
        return HasValue;
    }

    public static boolean Base_HasValue(List pValue) {
        Boolean HasValue = true;
        if (pValue == (null) || pValue.size() < 1) {
            HasValue = false;
        }
        return HasValue;
    }

    public static boolean Base_HasValue(Double pValue) {
        Boolean HasValue = true;
        if (pValue == (null)) {
            HasValue = false;
        }
        return HasValue;
    }


    public static void Base_CheckHasValue(String Msg, Object pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg, String pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg, Integer pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg, Map pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg, List pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg, Double pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }



}
