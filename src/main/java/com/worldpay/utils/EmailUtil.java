package com.worldpay.utils;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Properties;

@Component
public class EmailUtil {

//    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    
    private static final String MAIL_SMTP_HOST = "mail.smtp.host";
    
    private static final String MAIL_SMTP_PORT = "mail.smtp.port";
//    
//    private static final String MAIL_SMTP_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";
//    
//    private static final String MAIL_SMTP_SOCKETFACTORY_FALLBACK = "mail.smtp.socketFactory.fallback";
//    
//    private static final String MAIL_SMTP_SOCKETFACTORY_PORT = "mail.smtp.socketFactory.port";
    
    private static final String MAIL_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";
    
    private static final String MAIL_SMTP_SSL_PROTOCOLS = "mail.smtp.ssl.protocols";
    
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    
    private static final String MAIL_USER = "mail.user";
    
    private static final String MAIL_PASSWORD = "mail.password";
    
    private static final String TLSv = "TLSv1.2";
    
//    private static final String FALSE = "false";
    
    private static final String TRUE = "true";
    
    private static String EMAIL_HOST;
    
    private static String EMAIL_PORT;
    
    private static String USERNAME;
    
    private static String PASSWORD;

    @Value("${yunmail.host:}")
    public void setEmailHost(String emailHost) {
        EMAIL_HOST = emailHost;
    }

    @Value("${yunmail.port:}")
    public void setEmailPort(String emailPort) {
        EMAIL_PORT = emailPort;
    }

    @Value("${yunmail.username:}")
    public void setUsername(String username) {
        USERNAME = username;
    }

    @Value("${yunmail.password:}")
    public void setPassword(String password) {
        PASSWORD = password;
    }

    public static String getEmailHost() {
        return EMAIL_HOST;
    }

    public static String getEmailPort() {
        return EMAIL_PORT;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }
    
    
    public static void sendEmail(String toEmail, String title, String content) {
        try {
            // JDK 11 不需要手动添加 SSL Provider，直接注释或删除
            // Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            
            Properties props = System.getProperties();

            // SMTP 服务配置
            props.setProperty(MAIL_SMTP_HOST, EMAIL_HOST);
            props.setProperty(MAIL_SMTP_PORT, EMAIL_PORT);
            
            // 方案1：使用标准 SSL 配置（推荐）
            props.setProperty(MAIL_SMTP_SSL_ENABLE, TRUE);
            props.setProperty(MAIL_SMTP_SSL_PROTOCOLS, TLSv);
            
            // 方案2：如果上面的配置不行，可以改用 socket factory（二选一）
            // props.setProperty(MAIL_SMTP_SOCKETFACTORY_CLASS, SSL_FACTORY);
            // props.setProperty(MAIL_SMTP_SOCKETFACTORY_FALLBACK, FALSE);
            // props.setProperty(MAIL_SMTP_SOCKETFACTORY_PORT, EMAIL_PORT);
            
            props.setProperty(MAIL_SMTP_AUTH, TRUE);
            
            // 发件人账号配置（可选，因为下面 Authenticator 也会用）
            props.setProperty(MAIL_USER, USERNAME);
            props.setProperty(MAIL_PASSWORD, PASSWORD);
            
            // 建立邮件会话
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            // session.setDebug(true); // 调试时可开启
            
            // 创建邮件对象
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME)); // 发件人
            
            // 支持多个收件人（以逗号分隔）
            String[] toArray = toEmail.split(",");
            InternetAddress[] sendTo = new InternetAddress[toArray.length];
            for (int i = 0; i < toArray.length; i++) {
                sendTo[i] = new InternetAddress(toArray[i].trim());
            }
            message.setRecipients(Message.RecipientType.TO, sendTo);
            
            // 邮件标题和内容
            message.setSubject(title);
            message.setContent(content, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();
            
            // 发送邮件
            Transport.send(message);
            System.out.println("✅ 邮件发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ 异常：" + e.getMessage());
        }
    }


    // 简单测试
    public static void main(String[] args) {
        String to = "csz15621349306@gmail.com,1299681745@qq.com";
        String title = "充值订单审核通知";
        String content = "<b>测试测试测试，您有一条充值订单需审核</b><br>" +
                "充值人ID：888<br>" +
                "充值时间：2025-08-28 16:00<br>" +
                "充值金额：800 USD<br>" +
                "请尽快前往后台进行审核。";
        sendEmail(to, title, content);
    }
}
