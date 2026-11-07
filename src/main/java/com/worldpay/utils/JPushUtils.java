package com.worldpay.utils;

import com.worldpay.api.request.JpushReqEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 极光推送工具类（空壳，SDK 未接入时使用，后续对接时实现）
 */
public class JPushUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JPushUtils.class);

    public static void init(String appKey, String masterSecret, boolean environment) {
        LOG.info("[极光推送] 空壳初始化: appKey={}, environment={}", appKey, environment);
    }

    public static void shutdown() {
        LOG.info("[极光推送] 空壳关闭线程池");
    }

    public static void send(JpushReqEntity pushVo) {
        LOG.info("[极光推送] 空壳推送: {}", pushVo);
    }

    public static CompletableFuture<Void> sendAsync(JpushReqEntity pushVo) {
        return CompletableFuture.runAsync(() -> send(pushVo));
    }

    public static CompletableFuture<Void> sendToDeviceAsync(String registrationId, String title, String msg) {
        return CompletableFuture.runAsync(() -> sendToDevice(registrationId, title, msg));
    }

    public static CompletableFuture<Void> sendToUserAsync(String userId, String title, String msg) {
        return CompletableFuture.runAsync(() -> sendToUser(userId, title, msg));
    }

    public static CompletableFuture<Void> sendToAllAsync(String title, String msg) {
        return CompletableFuture.runAsync(() -> sendToAll(title, msg));
    }

    public static CompletableFuture<Void> sendToDevicesAsync(List<String> registrationIds, String title, String msg) {
        return CompletableFuture.runAsync(() -> LOG.info("[极光推送] 空壳批量推送到设备: count={}", registrationIds != null ? registrationIds.size() : 0));
    }

    public static CompletableFuture<Void> sendToUsersAsync(List<String> userIds, String title, String msg) {
        return CompletableFuture.runAsync(() -> LOG.info("[极光推送] 空壳批量推送到用户: count={}", userIds != null ? userIds.size() : 0));
    }

    public static void sendToUser(String userId, String title, String msg) {
        LOG.info("[极光推送] 空壳推送到用户: userId={}, title={}, msg={}", userId, title, msg);
    }

    public static void sendToDevice(String registrationId, String title, String msg) {
        LOG.info("[极光推送] 空壳推送到设备: registrationId={}, title={}, msg={}", registrationId, title, msg);
    }

    public static void sendToAll(String title, String msg) {
        LOG.info("[极光推送] 空壳广播推送: title={}, msg={}", title, msg);
    }
}
