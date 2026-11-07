package com.worldpay.utils;

import com.worldpay.entity.baidu.AddressInfo;

/** 地址解析空壳（SDK 未接入）*/
public class AddressExtractor {
    public static String extract(String raw) { return ""; }

    public static AddressInfo extractAll(String raw) {
        AddressInfo info = new AddressInfo();
        info.setProvince("");
        info.setCity("");
        info.setZipCode("");
        return info;
    }
}
