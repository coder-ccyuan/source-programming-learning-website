package com.cpy.api_system.utils;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求客户端工具类
 */
public class ApiClient {
private String accessKey;
private String secretKey;
public ApiClient(String accessKey, String secretKey){
    this.accessKey=accessKey;
    this.secretKey=secretKey;
}
public String getSign(HashMap<String,String> hashMap,String secretKey){
    Digester digester = new Digester(DigestAlgorithm.MD5);
    String content=hashMap.toString()+"."+secretKey;
    return digester.digestHex(content);
}

    private Map<String, String> getHeaderMap(String body) {
        HashMap<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        map.put("nonce", RandomUtil.randomNumbers(5));//随机数
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));//        map.put("body", body);
        map.put("sign", getSign(map,secretKey));//签名：加密secretKey
        return map;
    }

    /**
     *post
     * @param url
     * @param requestParams
     * @return
     */
    public String remoteInvokeByPost(String url,String requestParams){
        String result2 = HttpRequest.post(url)
                .addHeaders(getHeaderMap(requestParams))
                .body(requestParams)
                .execute().
                body();
        return result2;
    }
    /**
     *get
     * @param url
     * @param requestParams
     * @return
     */
    public String remoteInvokeByGet(String url,String requestParams){
        String result2 = HttpRequest.get(url)
                .addHeaders(getHeaderMap(requestParams))
                .body(requestParams)
                .execute().body();
        return result2;
    }
}
