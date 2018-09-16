package com.catslave.dbcp.catpools;

import com.catslave.dbcp.catpools.pool.ConnectionPool;

import java.lang.reflect.Method;

/**
 * @author cat.uncle
 * @date 2018/9/14
 */
public class ProxyConnection extends JdbcInterceptor {

    private final ConnectionPool pool;
    private PooledConnection connection;

    /**
     * {@link java.sql.Connection#close()} method name
     */
    public static final String CLOSE_VAL = "close";
    /**
     * {@link Object#toString()} method name
     */
    public static final String TOSTRING_VAL = "toString";
    /**
     * {@link java.sql.Connection#isClosed()} method name
     */
    public static final String ISCLOSED_VAL = "isClosed";
    /**
     * {@link javax.sql.PooledConnection#getConnection()} method name
     */
    public static final String GETCONNECTION_VAL = "getConnection";
    /**
     * {@link java.sql.Wrapper#unwrap(Class)} method name
     */
    public static final String UNWRAP_VAL = "unwrap";
    /**
     * {@link java.sql.Wrapper#isWrapperFor(Class)} method name
     */
    public static final String ISWRAPPERFOR_VAL = "isWrapperFor";

    /**
     * {@link java.sql.Connection#isValid(int)} method name
     */
    public static final String ISVALID_VAL = "isValid";

    /**
     * {@link java.lang.Object#equals(Object)}
     */
    public static final String EQUALS_VAL = "equals";

    /**
     * {@link java.lang.Object#hashCode()}
     */
    public static final String HASHCODE_VAL = "hashCode";

    public ProxyConnection(ConnectionPool pool, PooledConnection connection) {
        this.pool = pool;
        this.connection = connection;
    }

    /**
     * 通过动态代理的方式，每次对Connection的调用都过滤到invoke，然后通过method判断调用的是哪个方法，对应进行处理
     * 起到方法拦截器的作用，这是tomcat dbcp的实现方式。
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (compare(ISCLOSED_VAL, method)) {
            return Boolean.valueOf(isClosed());
        }
        if (compare(CLOSE_VAL, method)) {
            if (connection == null) return null; //noop for already closed.
            PooledConnection poolc = this.connection;
            this.connection = null;
            pool.returnConnection(poolc);
            return null;
        } else if (compare(TOSTRING_VAL,method)) {
            return this.toString();
        } else if (compare(GETCONNECTION_VAL,method) && connection!=null) {
            return connection.getConnection();
        }
        return super.invoke(proxy, method, args);
    }

    private boolean isClosed() {
        return connection == null || connection.isDiscarded();
    }

    public boolean compare(String methodName, Method method) {
        return compare(methodName, method.getName());
    }
}
