package com.yr.sql.common;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @moduleName: CacheKeyGenerator
 * @description: 本地缓存，自定义 keyGenerator
 * @author: 杨睿
 **/
@Component
class CacheKeyGenerator implements KeyGenerator {

    public static final String NO_PARAM_KEY = "ZERO";
    public static final String NULL_PARAM_KEY = "NULL";

    /**
     * 获取真实反射类型
     *
     * @return 反射类型
     */
    Class getActualClass(Object object) {
        Type type = object.getClass().getGenericSuperclass();

        // 判断是否泛型
        if (type instanceof ParameterizedType) {
            // 返回表示此类型实际类型参数的Type对象的数组
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            // 将第一个泛型T对应的类返回
            return (Class) types[0];
        } else {
            // 若没有给定泛型，则返回Object类
            return (Class) type;
        }
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        if (params.length == 0) {
            key.append(NO_PARAM_KEY).append(':');
        }
        for (Object param : params) {
            if (param == null) {
                key.append(NULL_PARAM_KEY);
            } else if (param instanceof ArrayList) {
                ArrayList list = (ArrayList) param;
                for (int i = 0; i < list.size(); i++) {
                    key.append(list.get(i).toString());
                    if (i != list.size() - 1)
                        key.append(',');
                }
            } else {
                key.append(param.toString());
            }
            key.append(':');
        }
        key.append(getActualClass(target).getSimpleName());
        return key.toString();
    }
}
