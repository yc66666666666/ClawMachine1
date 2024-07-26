package com.doll.utils;

import cn.hutool.core.util.RandomUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Before;

import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class PayUtils {


    //微信支付成功回调
    public static String notify_url = "https://upbaby.qzhuanyou.com/wxcallback";


    // 你的商户私钥
    public static final String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZnEVqoMxtwwyq\n" +
            "6xh6+KUmBev0xu4HBa0FVlJvyKnu3HzlQhNZ1PQim23lOZRJGhn80+KyGrxChdmT\n" +
            "+JJf3RJZvIk1+htyfRGtD7hLnrBUQhQylt3t41ZZxUcgnCgAnbSYw5283BBfWVhA\n" +
            "7RrGC8XTlfl9Odoo3FMaHj0XOfZsJRuP4F7fWzWCm/eBtDi8VRBeYyEzn48T4K09\n" +
            "a7c6oR5764ZYm8Aj6jRR1B1UEZTb/bvMmOIuJW7q1KXK/LrgoFepyngRUjoIBcoq\n" +
            "xsvsLqYSJ7onSp6U4dTKk68mysDmkpBItLlOH+c9lOioPVKcd0hp6QXq7qL0Mnop\n" +
            "4QQLQ3MfAgMBAAECggEAR3Up2aN51emxf+UjmzkF4n1IubK80elzBpOUf8nSO3q4\n" +
            "YxHs3JIzC9JQOh0h/Su2QSX0RDQptax8fdhZh1zZB4OsNL7ne8fwrBgu/IaSsm+W\n" +
            "473I+bwS1GnwX5wlA7xZYeNGArBAsYpngrSKCu05Fueiiv2Znf54biL4+lANcVrN\n" +
            "SdDCrxA9KU7sueD65XkofjelXCaH5OstRKmsDaxtaPpdMhcMQ7Up9inwOr8nbQs0\n" +
            "g3lGjZzAzNPMJe92qXZZiAgnitG78nhXwCAKy7Jy7tzJ9SM3H4Y2olJahtBs4wzT\n" +
            "YH7SVRdjXXAY0gf0d1nR/aJKzbcfJoHTb8SzBpGzQQKBgQDHhBVMuqNVftjdv7oa\n" +
            "BuMm3ax7UWuxj7lwr4ulWsBKUhGVVAtRQ/uWFnui7dj7GaaG4RXSeYelaY3Fk7eD\n" +
            "ttoW/8S2kQILfJZ6a7GwQgIGD1c63lrc9i8aAEcZ5zyK8MW3Rp4hIZwOAe48jCaM\n" +
            "VFP0Xt6EZOcFKJUVmaST/Lb0oQKBgQDFGS+jR26kitpI1UR4FTZpPledMcPqc8dK\n" +
            "ZNKew+daQIq/aa3YSwHgD8ysPP80nYf9HbYMPzTlfuuxWrerlqiT28+h2mK/5TjD\n" +
            "OgqySMiaUtckCPu+ve6oab3tYcpi58m4bM3pSA0cYIbjLSMFiLNGi5jZPSCftGFl\n" +
            "hiq66f2PvwKBgG8pFjzz18DwtZ+HDQcnQzQKWekzSg9aBnXUOLfeQjimVQmgE9Yp\n" +
            "TGkpCaWTY0sz1WZczi3AbGUq1ER6ssp9/DDIPC+Dx2Hi/mwiyJNtk3cQ4Qb5kCKA\n" +
            "P2n7MaIrBb5bu60LeVr4QNxFExlE9M1Gxqfmm4+kMFrda4LnYZzaNo1BAoGBALdx\n" +
            "0qHQyU0CI1pojLqLW8ImkRswh10+dz1pi5LTWQ/qRHkVXgWTAmLNsbyZZRpOiJRw\n" +
            "Q9EwPwZe0/WVLp/YsHw1uRG2lfRu3x7tdaksDvF7qtvSNKT6WBeNm4VGYD0f6OLq\n" +
            "+ddnBTTlLoZgq1jqO5msMg33U9bw5zm73ydqw2I3AoGBALpSa31D4Ibr3i1h5qyW\n" +
            "X3BlKZwIrAfzskXf0/W1yF5YncXgkGhZ4j/LfKjqrbN+vbZ/P7mhV/QtpgXTcXgu\n" +
            "kcbiHb4vvPFIpCcAgBnd5JmKgVMKFiDGPOwwXrXm1BwV4GCkKEmMD19+XGUJiGI4\n" +
            "ub8sNlqzhbPvVnyZ5qOA1KVd\n" +
            "-----END PRIVATE KEY-----";

    public static final String merchantId = "1680608250"; // 商户号
    public static final String merchantSerialNumber = "36EB192FE0C3489797917B03A6807B8E6DCB2DA7"; // 商户证书序列号
    public static final String apiV3Key = "aswvndrbonrbnoarnbren23nfoso492s"; // API V3密钥

    public static final String appId = "wxd937a072abdf46f2";   //appid

    public static final String  appSecret = "555f1bc8865894eeb45e5c23966c668e";

    public static final String package1 = "Sign=WXPay"; //签名固定字符串

    //申请授权码地址
    public static final String wxOAuth2RequestUrl = "https://open.weixin.qq.com/connect/oauth2/authorize";




//    public static PrivateKey privateKey2;
//
//    static {
//        try {
//            // 假设你的私钥是以Base64编码的字符串形式存在
//            String privateKeyPEM = "YOUR_PRIVATE_KEY_STRING";
//
//            // 去掉PEM头和尾
//            privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
//                    .replace("-----END PRIVATE KEY-----", "")
//                    .replaceAll("\\s", "");
//
//            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
//
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//            privateKey2 = keyFactory.generatePrivate(keySpec);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


        public static String wxOAuth2CodeReturnUrl;

        static {
            try {
                wxOAuth2CodeReturnUrl = URLEncoder.encode("https://upbaby.qzhuanyou.com/wx-oauth-code-return", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }





//    public static Map createOrder() throws IOException, IllegalBlockSizeException {
//
//        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(PayUtils.privateKey.getBytes(StandardCharsets.UTF_8)));
//
//        //使用自动更新的签名验证器，不需要传入证书
//        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
//                new WechatPay2Credentials(PayUtils.merchantId, new PrivateKeySigner(PayUtils.merchantSerialNumber, merchantPrivateKey)),
//                PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));
//
//        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
//                .withMerchant(PayUtils.merchantId, PayUtils.merchantSerialNumber, merchantPrivateKey)
//                .withValidator(new WechatPay2Validator(verifier))
//                .build();
//
//
//        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/app");
//        httpPost.addHeader("Accept", "application/json");
//        httpPost.addHeader("Content-type", "application/json; charset=utf-8");
//
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        ObjectNode rootNode = objectMapper.createObjectNode();
//        rootNode.put("mchid", PayUtils.merchantId)
//                .put("appid", PayUtils.appId)
//                .put("description", "Image形象店-深圳腾大-QQ公仔")
//                .put("notify_url", PayUtils.notify_url)
//                .put("out_trade_no", System.currentTimeMillis() + "111168");
//        rootNode.putObject("amount")
//                .put("total", 1);
////        rootNode.putObject("payer")
////                .put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
//
//        objectMapper.writeValue(bos, rootNode);
//
//        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
//        CloseableHttpResponse response = httpClient.execute(httpPost);
//
//        String bodyAsString = EntityUtils.toString(response.getEntity());
//        System.out.println(bodyAsString);
//
//
//        //部分2
//        String timestamp = System.currentTimeMillis() + "";
//        String nonce = RandomUtil.randomString(32);
//        StringBuilder builder = new StringBuilder();
//        //应用id
//        builder.append(PayUtils.appId).append("\n");
//        //时间戳
//        builder.append(timestamp).append("\n");
//        //随机字符串
//        builder.append(nonce).append("\n");
//
//        JsonNode node = objectMapper.readTree(bodyAsString);
//        //预支付交易会话ID
//        builder.append(node.get("prepay_id")).append("\n");
//
//        String ciphertext = RsaCryptoUtil.encryptOAEP(builder.toString(), verifier.getValidCertificate());
//        System.out.println(ciphertext);
//        Map map=new HashMap();
//        map.put("noncesstr",nonce);
//        map.put("package",PayUtils.package1);
//        map.put("timestamp",timestamp);
//        map.put("sign",ciphertext);
//        return map ;
//    }

    public static boolean signVerifier(String serialNumber,String message,String signature) {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(PayUtils.privateKey.getBytes(StandardCharsets.UTF_8)));
        //使用自动更新的签名验证器，不需要传入证书
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(PayUtils.merchantId, new PrivateKeySigner(PayUtils.merchantSerialNumber , merchantPrivateKey)),
                PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));
        return verifier.verify(serialNumber, message.getBytes(StandardCharsets.UTF_8), signature);
    }


    public static String decryptOrder(String body) throws IOException, GeneralSecurityException {
        AesUtil aesUtil=new AesUtil(PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));
        ObjectMapper objectMapper=new ObjectMapper();
        JsonNode node=objectMapper.readTree(body);
        JsonNode resource=node.get("resource");
        String ciphertext=resource.get("ciphertext").textValue();
        String associated_data=resource.get("associated_data").textValue();
        String nonce=resource.get("nonce").textValue();
        return aesUtil.decryptToString(associated_data.getBytes(StandardCharsets.UTF_8),nonce.getBytes(StandardCharsets.UTF_8),ciphertext);

    }


    public static void CloseOrder(String outTradeNo) throws Exception {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(PayUtils.privateKey.getBytes(StandardCharsets.UTF_8)));

        //使用自动更新的签名验证器，不需要传入证书
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(PayUtils.merchantId, new PrivateKeySigner(PayUtils.merchantSerialNumber, merchantPrivateKey)),
                PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));

        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(PayUtils.merchantId, PayUtils.merchantSerialNumber, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier))
                .build();




        //请求URL
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/"+outTradeNo+"/close");
        //请求body参数
        String reqdata ="{\"mchid\": \""+PayUtils.merchantId+"\"}";

        StringEntity entity = new StringEntity(reqdata,"utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) {
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }
    }


    public static void QueryOrder(String outTradeNo) throws Exception {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(PayUtils.privateKey.getBytes(StandardCharsets.UTF_8)));

        //使用自动更新的签名验证器，不需要传入证书
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(PayUtils.merchantId, new PrivateKeySigner(PayUtils.merchantSerialNumber, merchantPrivateKey)),
                PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));

        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(PayUtils.merchantId, PayUtils.merchantSerialNumber, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier))
                .build();

        //请求URL
        URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/"+outTradeNo);
        uriBuilder.setParameter("mchid",PayUtils.merchantId);

        //完成签名并执行请求
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) {
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }
    }







}
