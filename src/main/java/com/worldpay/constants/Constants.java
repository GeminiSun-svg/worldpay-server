package com.worldpay.constants;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author csz
 *
 */
public interface Constants {
	
	public static final String MSG_KEY = "新版本提醒";
		
    public static final String WWW = "www.";

    public static final String HTTP = "http://";

    public static final String HTTPS = "https://";
    
    public static final String APP_PACKAGE_NAME = "uCard";
    
    public String FILE_NAME = "uCardFile";
    
    public String ID_CARD = "idCard";
    
	String  SUCCESS="操作成功";
	
	String  API_RES="success";
	
	String  API_RES_W="SUCCESS";
	
	String  API_RES_F="FAIL";
	
	String  API_RES_E="ERROR";
	
	String ERROR = "操作失败，请稍后再试";
	
	String ERROR_EN = "操作失败，请稍后再试";

    String NULL_ID="Id不能为空";

    String NULL_LANGUAGE="语言不能为空";

	String NULL_CONTETN="没有相关数据";
	
	String NULL_PARAMS="参数不能为空";

    String Invalid_registrationId="无效的registrationId";
	// 响应请求成功
	String HTTP_RES_CODE_200_VALUE = "操作成功";
	
	String EXCEPTION_MSG = "系统繁忙，请稍后再试！！！";
	
	String ADMIN_STR = "admin";
	
	String DEFAULT_PASSWORD = "qwer1234";
	
	String  USER_WALLET_BALANCE="user_wallet_balance";
	
	int PAGENUMBER = 1;
	
	int PAGESIZE =10;
	
	Integer OPERATE_COUNT = 0;
	
	Double ZERO = 0.0;
	
	Integer ZERO_INT = 0;
	
	int CONTANTS_ONE =1;
	
	Integer JWT_EXPIRE_TIME = 24 * 60 * 60 * 1000;
	
	long REDIS_EXPIRE_TIME = 24 * 60 * 60 * 1000;
	
	long EXPIRATIONTIME = 5;
	
	Integer USER_JWT_EXPIRE_TIME = 240 * 60 * 60 * 1000;
	
	long USER_REDIS_EXPIRE_TIME = 240 * 60 * 60 * 1000;
	
	long CODE_EXPIRE_TIME = 10 * 60 * 1000;
	
	long TRANSFER_EXPIRE_TIME = 25 * 60 * 1000;
	
	String TRANSFER_TOKEN_BEARER = "transfer_token";
	
	// 响应请求成功code
	Integer HTTP_RES_CODE_200 = 200;
	// 系统错误
	Integer HTTP_RES_CODE_500 = 500;

	Integer HTTP_RES_CODE_404 = 404;
	
	Integer HTTP_RES_CODE_403 = 403;
	
	Integer HTTP_RES_CODE_400 = 400;
	
	Integer HTTP_RES_CODE_601 = 601;

	Integer HTTP_RES_CODE_602 = 602;
	
    /**
     * AUTH_HEADER_START_WITH 请求中token前缀
     */
    public static final String AUTH_HEADER_START_WITH = "Bearer ";

    /**
     * SIGNING_KEY token生成密钥（HS512 要求至少 64 字节）
     */
    public static final String SIGNING_KEY = "Boss@Jwt!&Secret^#Boss@Jwt!&Secret^#Boss@Jwt!&Secret^#Boss@Jwt!&";

    /**
     * JWT 签名/校验统一使用的 Key（hs512）
     */
    static javax.crypto.SecretKey jwtKey() {
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(SIGNING_KEY.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    /**
     * HEADER_AUTH 请求header中token的key
     */
    public static final String HEADER_AUTH = "token";

    
    //配置前端响应头
    public static final String HEADER_ACCESS = "Access-Control-Expose-Headers";
    
    public static final String user_googleKey = "3EDUIKMIXDQIZQIH";
    
    public static final String GOOGLE_COCE = "googleCoce";
    
    public static final String MOBILEPREFIX = "86";
    
    public static final Integer CARDID = 261;
    
//    public static final String MSG_IP = "154.82.73.134";
    
    public static final List<String> MSG_IP_LIST = Arrays.asList(
            "95.40.130.189",
            "43.198.127.163",
            "127.0.0.1"
    );
    

}
