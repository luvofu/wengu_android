package com.culturebud.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by 郝泽亮 on 2017/4/29.
 */

public class TxtUtil {
    private static boolean isMatchPatternWithString(String patternDesc,String content) throws PatternSyntaxException {
       try {
           String regExp = patternDesc;
           Pattern p = Pattern.compile(regExp);
           Matcher m = p.matcher(content);
           return m.matches();
       } catch (PatternSyntaxException e) {
           e.printStackTrace();
       }

       return false;
    }

    public static boolean isChinaPhoneLegal(String phoneNum)  {
//        String patternDesc = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
//
//        return isMatchPatternWithString(patternDesc, str);

        //因为防止新的手机号没有判断到，所以在此不去校验实际手机号的合法性，只要校验是11位数字即可，真实有效性可以让后台第三方发送短信的API去检验.
        if (phoneNum.length() == 11 && isValidateNumber(phoneNum)) {
            return true;
        }

        return false;
    }

    public static boolean isMatchWenyaAccountRule(String account)  {
        String patternDesc = "^(?![_-])(?!.*[_-]$)[a-zA-Z0-9_-]{6,20}$";

        return isMatchPatternWithString(patternDesc, account);
    }

    public static boolean isValidateEmail(String email) {
        String patternDesc = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

        return isMatchPatternWithString(patternDesc, email);
    }

    public static boolean isValidateNumber(String numberString) {
        String patternDesc = "^[0-9]*$";

        return isMatchPatternWithString(patternDesc, numberString);
    }
}
