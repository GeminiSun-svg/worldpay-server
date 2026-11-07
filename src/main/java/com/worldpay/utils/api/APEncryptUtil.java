package com.worldpay.utils.api;
import com.google.common.base.Strings;

/**
 * encrypt util
 */
public class APEncryptUtil {

    /**
     * encode
     *
     * @param secret
     * @param content
     * @return
     * @throws Exception 
     */
    public static String encode(String secret, String content) throws Exception {
//        String base64String = Base64.encode(content);
//        System.out.println("base64String=" + base64String);
        String aesString = AesAPIUtils.encrypt(content, secret);
//        System.out.println("aesString=" + aesString);
        return MD5Util.digest(aesString);
    }

    /**
     * decode
     *
     * @param secret
     * @param content
     * @return
     * @throws Exception 
     */
    public static String decode(String secret, String content) throws Exception {
        if (Strings.isNullOrEmpty(content)) {
            return null;
        }
        String base64String = AesAPIUtils.decrypt(content, secret);
        return base64String;
    }


    public static void main(String[] args) throws Exception {
        String decode = decode("b635dd5c87f7bf73387929203321b1e1", "d4765efa8fbc6b21dcee653013f82ef0fc5f52f7d0f56ff7a97221cd9bd8b067c51cba5ca49e1c8cb690c60ce3df24b5228b2f65a7362213eceae4ed6338c6758a7bbf2f20f4a8f0e8bfc1801772b7f2a43bff9476ed376390d2b600184c51c2");
        System.out.println(decode);
    }
}
