package com.cpy.clientApi.Decoder;

import com.cpy.common.BaseResponse;
import com.cpy.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * openFeign 自定义解码器
 * @Author:成希德
 */
public class MyDecoder implements Decoder {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object decode(Response response, Type type) throws FeignException, IOException {

        TypeFactory typeFactory = objectMapper.getTypeFactory();

        BaseResponse baseResponse = objectMapper.readValue(response.body().asInputStream(),
                typeFactory.constructParametricType(BaseResponse.class, typeFactory.constructType(type)));

        if (type instanceof BaseResponse) {
            return baseResponse;
        }

        if (baseResponse.getCode()==0) {
            return baseResponse.getData();
        }

        throw new BusinessException(baseResponse.getCode(),baseResponse.getMessage());
    }
}
