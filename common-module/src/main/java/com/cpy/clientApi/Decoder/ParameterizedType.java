package com.cpy.clientApi.Decoder;

import java.lang.reflect.Type;

public interface ParameterizedType extends Type {
    /**
     * 返回里面的泛型，比如List<String>, 那么这个方法返回String，如果是Map<String, Integer>那么这个方法返回{String, Integer}的数组
     * @since 1.5
     */
    Type[] getActualTypeArguments();

    /**
     * 返回当前这个类的类型，比如List<String>, 那么这个方法返回List，如果是Map<String, Integer>那么这个方法返回Map
     */
    Type getRawType();

    /**
     * 如果是内部类的情况，这个方法返回的是最外层的类，也就是封闭类，比如O<T>.I<S>这种类型，返回的是O<T>
     */
    Type getOwnerType();
}
