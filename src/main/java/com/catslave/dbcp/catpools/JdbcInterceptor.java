package com.catslave.dbcp.catpools;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author cat.uncle
 * @date 2018/9/14
 */
public abstract class JdbcInterceptor implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

    public boolean compare(String name1, String name2) {
        return name1 == name2;
    }
}
