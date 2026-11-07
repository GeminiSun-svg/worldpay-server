package com.worldpay.utils.api;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class CheckUtil {

    /**
     * 校验手机号
     *
     * @param areaCode 电话区号
     * @param mobile 手机号
     * @param region 国家地区2位数编码 US
     * @return
     */
    public static boolean validatePhoneNumber(String areaCode, String mobile, String region) {
        try {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(areaCode + mobile, region);
            boolean validNumber = phoneNumberUtil.isValidNumber(number);
            PhoneNumberUtil.PhoneNumberType numberType = phoneNumberUtil.getNumberType(number);
            if (validNumber) {
                if (numberType.equals(PhoneNumberUtil.PhoneNumberType.MOBILE)
                        || numberType.equals(PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static void main(String[] args) {
        boolean b = validatePhoneNumber("+7", "9214263174", "RU");
        System.out.println(b);
    }
}
