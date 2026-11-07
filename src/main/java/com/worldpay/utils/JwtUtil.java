package com.worldpay.utils;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.worldpay.constants.Constants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
	// Token过期时间30分钟
    public static final long EXPIRE_TIME = 120 *60* 60 * 1000;

    /* *
     * @Author lsc
     *  校验token是否正确 
     * @Param token
     * @Param username
     * @Param secret
     * @Return boolean
     */
    @SuppressWarnings("unused")
	public static boolean verify(String token, String username, String secret) {
        try {
            // 设置加密算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
    /* *
     * @Author lsc
     * 生成签名,30min后过期 
     * @Param [username, secret]
     * @Return java.lang.String
     */
    public static String sign(String username, String secret) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(date)
                .sign(algorithm);

    }

    /* *
     * @Author lsc
     *  获得用户名 
     * @Param [request]
     * @Return java.lang.String
     */
    public static String getUserNameByToken(String token)  {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username")
                    .asString();
        }catch (Exception e) {
            return "";
        }

    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUserNameByJwt(String token) {
        return  Jwts.parser()
                .verifyWith(Constants.jwtKey())
                .build()
                .parseSignedClaims(token.replace(Constants.AUTH_HEADER_START_WITH, "")).getPayload().getSubject();
    }

    public static void main(String[] args) {
		System.out.println(JwtUtil.sign("1111", "1111"));
	}

}
