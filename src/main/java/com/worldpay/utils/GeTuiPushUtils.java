package com.worldpay.utils;

import org.springframework.stereotype.Component;

/** 推送工具空壳（SDK 未接入，后续对接极光/个推时实现）*/
@Component
public class GeTuiPushUtils {
    public void pushNotice(Object... args) {}
    public void pushNoticeByTag(Object... args) {}
    public void pushAll(String title, String content, Object extra) {}
}
