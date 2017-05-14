package com.culturebud.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by 郝泽亮 on 2017/4/29.
 */

public class TxtUtil {
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isMatchWenyaAccountRule(String account) throws PatternSyntaxException {
        String regExp = "^(?![_-])(?!.*[_-]$)[a-zA-Z0-9_-]{6,20}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(account);
        return m.matches();
    }
}
