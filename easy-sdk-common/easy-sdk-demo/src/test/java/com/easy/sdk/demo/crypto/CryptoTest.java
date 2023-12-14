package com.easy.sdk.demo.crypto;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.json.JSONUtil;
import com.easy.sdk.demo.EasySdkDemoApplication;
import com.easy.sdk.demo.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/12/06 14:13 PM
 * @description 接口参数解密测试类
 * 在启动类增加@EnableCrypto
 * 使用方式：
 *   - 配置yaml文件中的属性
 *   - 在method-> 参数前添加注解 @Crypto(CryptoTypeEnum)
 *   - 注：可用于String类型的参数或Bean中 String类型的属性
 *   - 范围：String String[] List<String> Set<String> Map<String,String> Bean(String 字段)
 *
 */

@SpringBootTest(classes = EasySdkDemoApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class CryptoTest {

    /**
     * rsa私钥和公钥
     */
    private static String privateKey;
    private static String publicKey;

    /**
     * 生成公私钥
     */
    @Before
    public void init() {
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        privateKey = Base64.encode(pair.getPrivate().getEncoded());
        System.out.println("RSA 私钥:" + privateKey);
        publicKey = Base64.encode(pair.getPublic().getEncoded());
        System.out.println("RSA 公钥:" + publicKey);
    }



    /**
     * des 加密解密 接口验证
     */
    @Test
    public void cryptoDesTest() {
        String url = "http://localhost:8080/demo/checkCrypto";
        String result = null;
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("name", "zhangsan");
            paramMap.put("pwd", "223bb1e6003a014b");
            result = OkHttpUtils.sendGet(url, paramMap, new HashMap<>());
        } catch (IOException e) {
            log.error("sendGet error:{}", e.getMessage());
        }
        log.info(result);
    }

    /**
     * 2023-12-11 14:47:15.386 []  INFO 81693 --- com.easy.sdk.demo.crypto.CryptoTest : {"retCode":"0000","retMsg":"ok","body":{"id":123456,"name":"zhangsan","age":33,"pwd":"123456"}}
     */


    /**
     * rsa 加密解密 接口验证
     */
    @Test
    public void cryptoRsaTest() {
        String url = "http://localhost:8080/demo/checkCrypto";
        String result = null;
        try {
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("name", "zhangsan");
            bodyMap.put("pwd", "mvsOgSp6LiEk94hZRmInN+idSBDoOqZv6PfSLiR3i5yBNqqVYW1FV8E5qMn1ZUf857VI11I8QhzbWjxsMvjk6BX+EY641d2YLbg4bR8F+pQD2m01MXMo8MgWrx2Wjo5Ymu4kvqeUqn87d54IPstm/S7IDh3fF2oZn41Ec30fbgs=");
            result = OkHttpUtils.sendPostWithJson(url, new HashMap<>(), bodyMap);
        } catch (IOException e) {
            log.error("sendPost error:{}", e.getMessage());
        }
        log.info(result);
    }

    /**
     * 2023-12-11 14:46:25.268 []  INFO 81397 --- com.easy.sdk.demo.crypto.CryptoTest : {"retCode":"0000","retMsg":"ok","body":{"id":123456,"name":"zhangsan","age":33,"pwd":"123456"}}
     */


    /**
     * rsa 加密解密
     * 1. 生成密钥
     * 2. 加密
     * 3. 解密
     */
    @Test
    public void rsaTest() {

        String text = "123456";

        // 初始化对象
        // 第一个参数为加密算法，不传默认为 RSA/ECB/PKCS1Padding
        // 第二个参数为私钥（Base64字符串）
        // 第三个参数为公钥（Base64字符串）
//        RSA rsa = new RSA(AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue(), privateKey, publicKey);
        RSA rsa = new RSA(privateKey, publicKey);

        // 公钥加密，私钥解密
        String encryptByPublic = rsa.encryptBase64(text, KeyType.PublicKey);
        System.out.println("RSA 公钥加密:" + encryptByPublic);
        String decryptByPrivate = rsa.decryptStr(encryptByPublic, KeyType.PrivateKey);
        System.out.println("RSA 私钥解密:" + decryptByPrivate);

        // 私钥加密，公钥解密
        String encryptByPrivate = rsa.encryptBase64(text, KeyType.PrivateKey);
        System.out.println("RSA 私钥加密:" + encryptByPrivate);
        String decryptByPublic = rsa.decryptStr(encryptByPrivate, KeyType.PublicKey);
        System.out.println("RSA 公钥解密:" + decryptByPublic);


        //使用私钥解密公钥加密后的密文
        // 传入私钥以及对应的算法
        rsa = new RSA(AsymmetricAlgorithm.RSA_ECB_PKCS1.getValue(), privateKey, null);

        // 私钥解密公钥加密后的密文
        String decrypt = rsa.decryptStr(encryptByPublic, KeyType.PrivateKey);
        System.out.println("RSA 使用私钥解密公钥加密后的密文:" + decrypt);
    }


    /**
     * rsa 加密解密
     * 1. 生成密钥
     * 2. 加密
     * 3. 解密
     */
    @Test
    public void rsa2Test() {
        // 初始化对象
        // 第一个参数为加密算法，不传默认为 RSA/ECB/PKCS1Padding
        // 第二个参数为私钥（Base64字符串）
        // 第三个参数为公钥（Base64字符串）
        privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKrM2QZMpHlQnHej5YRFERKx6Pnn2Ge4ncfJI0bKydhajDFcos72zFtupngnm5FJ0+UzBspyiLYRSZswS5AwMfKmRTqJ5yW0g4+VmC6usWjedMjNXPPGWy/D4UL3SdkXh2apjjuLRiDDKbqfKQJZG1fvUDZrZOER3x7IRvH/7TYRAgMBAAECgYAfOSBpXuRoSvy3gE4qTmfvF3fkC2aLm3u4dIQO4m5grzjWoz0eAY7dHg6vg2oUdr8WNKxGoQZt5h4jEQKXQsA2c4HPupgKSJPRPOv5vB9uwYT6C5QfhhsrGAxf8BNbPrE38lN6sTXT8+OV0jKkB0RslhFqB9ZQUXWOde+Qj3SSOQJBANf9iQYQPxR6Ly/hOBXBALoztTvuBWNvFUJCqfE2nZuayIJsgd/oun7ehJk9PH+XgxxDl8P70jHvl6/p0vetLfUCQQDKcFoOvJwv36CXsKVKNHS6ejPaov4F3vf10daxaJoBL9wu8X9G8fqRL5lYs5+aUehzfchpldIkEgR476cahFotAkEAmOM4ePhLDn2mpSexAYUKtMMhSHili8r3q11w2fOlAJ5BzEBudkZQrLlp9uanylE/V2RpKys3kG1eXAEReow1eQJBAIwmlHK3aXpLXCcoiIaDIn0ntVacmTj/ULPC/tv8CwLfW5S0ZOV9CXgSeBvM36Y7Jq8uWd2fd8i5IJeJcysMjlECQQCCJVBPQBomYHch43LLYwgS1xtM56alDtgeq8Cx5aQxM9EZzFIcY96zcUYIt/VdOLlYyH/KzUq9wpmgmd0NDoQn";
        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqzNkGTKR5UJx3o+WERRESsej559hnuJ3HySNGysnYWowxXKLO9sxbbqZ4J5uRSdPlMwbKcoi2EUmbMEuQMDHypkU6iecltIOPlZgurrFo3nTIzVzzxlsvw+FC90nZF4dmqY47i0Ygwym6nykCWRtX71A2a2ThEd8eyEbx/+02EQIDAQAB";
        String text = "123456";
        RSA rsa = SecureUtil.rsa(privateKey, publicKey);
        //公钥加密
        String encryptByPublic = Base64.encode(rsa.encrypt(text, KeyType.PublicKey));
        System.out.println("RSA 公钥加密:" + encryptByPublic);
        //私钥解密
        String decryptByPrivate = rsa.decryptStr(encryptByPublic, KeyType.PrivateKey, Charset.forName("UTF-8"));
        System.out.println("RSA 私钥解密:" + decryptByPrivate);
    }


    /**
     * des 加密解密
     * 1. 生成密钥
     * 2. 加密
     * 3. 解密
     */
    @Test
    public void desTest() {
        //待加密文本
        String content = "123456";

        //随机生成密钥 secret-key
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue()).getEncoded();
        String encryptByKey = Base64.encode(key);
        System.out.println("DES 密钥:" + encryptByKey);
        //指定生成密钥 secret-key
//        byte[] key = "0d850b5500d11b0c".getBytes();

        //构建 des
        DES des = SecureUtil.des(key);

        //加密解密
        byte[] encrypt = des.encrypt(content);
        byte[] decrypt = des.decrypt(encrypt);

        //加密为16进制，解密为原字符串
        String encryptHex = des.encryptHex(content);
        String decryptStr = des.decryptStr(encryptHex);
        System.out.println("DES 加密：" + encryptHex);
        System.out.println("DES 解密：" + decryptStr);
        System.out.println("DES 解密：" + new String(decrypt));

    }
}
