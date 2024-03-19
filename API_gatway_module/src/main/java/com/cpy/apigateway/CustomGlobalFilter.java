package com.cpy.apigateway;


import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.cpy.clientApi.InterfaceClient;
import com.cpy.clientApi.UserClient;
import com.cpy.model.dto.user.UserSecretKeyRequest;
import com.cpy.common.BaseResponse;
import com.cpy.model.dto.interfaceInfo.InterfaceInformationQueryRequest;
import com.cpy.model.entity.InterfaceInformation;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 全局过滤
 * 请求日志
 * 黑白名单
 */
@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 白名单
     */
    public static final List<String> WHITE_LIST = Arrays.asList("127.0.0.1");
    @Resource
    private UserClient userClient;
    @Resource
    private InterfaceClient interfaceClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        //1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识" + request.getId());
        log.info("请求地址" + request.getPath().value());
        log.info("请求方法" + request.getMethodValue());
        log.info("请求参数" + request.getQueryParams());
        String hostString = request.getLocalAddress().getHostString();
        log.info("请求来源地址" + hostString);
        //2.黑白名单
        if (!WHITE_LIST.contains(hostString)) {
            handlerNoAuth(response);
        }

        //3.用户鉴权 判断 ak sk 从请求头中拿取数据
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        //todo 在redis中判断随机数是否存在
        String timestamp = headers.getFirst("timestamp");
        //判断时间不超过5min
        Long FIVE_MINUTES = (long) 60 * 5;
        long cur = (System.currentTimeMillis() - Long.parseLong(timestamp)) / 1000;
        if (cur >= FIVE_MINUTES) {
            return handlerNoAuth(response);
        }
        String sign = headers.getFirst("sign");

        //拼接数据与sign进行匹配
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        map.put("nonce", nonce);
        map.put("timestamp", timestamp);
        Digester digester = new Digester(DigestAlgorithm.MD5);
        //获取secretKey
        String secretKey = getSecretKey(accessKey);
        String content = map.toString() + "." + secretKey;
        String s = digester.digestHex(content);
        if (!s.equals(sign)) {
            return handlerNoAuth(response);
        }
        //4.请求模拟接口是否存在
        String url=request.getURI().toString();
        InterfaceInformationQueryRequest interfaceInformationQueryRequest = new InterfaceInformationQueryRequest();
        interfaceInformationQueryRequest.setUrl(url);
        String s1 = JSONUtil.toJsonStr(interfaceInformationQueryRequest);
//        String res = HttpRequest.post("http://localhost:8092/interfaceInfo/query/url").body(s1).execute().body();
//        InterfaceInformation interfaceInformation = JSONUtil.toBean(res, InterfaceInformation.class);
        InterfaceInformationQueryRequest request1 = new InterfaceInformationQueryRequest();
        request1.setUrl(url);
        InterfaceInformation interfaceInformation = interfaceClient.queryByUrl(request1);
        if (interfaceInformation==null||interfaceInformation.getStatus()==1){
            handlerInterfaceNotExist(response);
        }
        //5.请求转发调用接口
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                //6.响应日志
                                log.info("响应：" + response.getStatusCode());
                                //todo 7.调用成功更改调用次数

                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                StringBuilder sb2 = new StringBuilder(200);
                                sb2.append("<--- {} {} \n");
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                //rspArgs.add(requestUrl);
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                sb2.append(data);
                                log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return -1;
    }

    public Mono<Void> handlerNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handlerError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
    public Mono<Void> handlerInterfaceNotExist(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
        return response.setComplete();
    }
    public String getSecretKey(String accessKey){
        //原始获取
//        String url="http://localhost:8092/user/get/secretKey";
//        UserSecretKeyRequest userSecretKeyRequest = new UserSecretKeyRequest();
//        userSecretKeyRequest.setAccessKey(accessKey);
//        String body = HttpRequest.post(url).body(JSONUtil.toJsonStr(userSecretKeyRequest)).execute().body();
        //通过feign获取
        UserSecretKeyRequest userSecretKeyRequest = new UserSecretKeyRequest();
        userSecretKeyRequest.setAccessKey(accessKey);
        String secretKey = userClient.getSecretKeyByAccessKey(userSecretKeyRequest);
        if (secretKey==null)return "";
        return secretKey;
    }
}