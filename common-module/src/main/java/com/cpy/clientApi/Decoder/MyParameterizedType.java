package com.cpy.clientApi.Decoder;

import com.cpy.common.BaseResponse;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author:成希德
 */
public class MyParameterizedType implements ParameterizedType {

    private Type type;

    /**
     * 将Feign Client方法的返回值注入，只要两种类型，一种是ParameterizedTypeImpl，另一种是具体的Class对象
     */
    public MyParameterizedType(Type type) {
        this.type = type;
    }

    /**
     * 属性Type就是BaseResponse的泛型类型，直接返回type就可以
     */
    @Override
    public Type[] getActualTypeArguments() {
        Type[] types = new Type[1];
        types[0] = type;
        return types;
    }

    /**
     * 最外层的类型就是我们要与type合并的BaseResponse类型
     */
    @Override
    public Type getRawType() {
        return BaseResponse.class;
    }

    /**
     * 这个Owner一般没用到，如果type是个内部类静态类情况下，需要返回最外部的类型，这里直接调用Class对象获取封闭类的方法
     */
    @Override
    public Type getOwnerType() {
        if (type instanceof ParameterizedTypeImpl) {
            ParameterizedTypeImpl typeImpl = (ParameterizedTypeImpl) type;
            return typeImpl.getRawType().getEnclosingClass();
        }

        if (type instanceof Class) {
            return ((Class) type).getEnclosingClass();
        }

        return null;
    }
}

