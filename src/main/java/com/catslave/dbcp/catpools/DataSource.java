package com.catslave.dbcp.catpools;

import com.catslave.dbcp.catpools.pool.ConnectionPool;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * @author cat.uncle
 * @date 2018/9/13
 */
public class DataSource implements javax.sql.DataSource {

    private ConnectionPool pool;
    private ConnectionProperties properties;

    public DataSource() {

    }

    public ConnectionProperties getProperties() {
        return properties;
    }

    public void setProperties(ConnectionProperties properties) {
        this.properties = properties;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (pool == null) {
            pool = new ConnectionPool(getProperties());
            pool.init();
        }
        return setupConnection(pool.borrowConnection());
    }

    private Connection setupConnection(PooledConnection pooledConnection) {
        try {
            Class<?> proxyClass = Proxy.getProxyClass(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class, javax.sql.PooledConnection.class});
            Constructor<?> proxyClassConstructor = proxyClass.getConstructor(new Class[]{InvocationHandler.class});
            Connection connection = (Connection) proxyClassConstructor.newInstance(
                    new Object[]{new ProxyConnection(pool, pooledConnection)});
            return connection;
        } catch (
                NoSuchMethodException e)

        {
            e.printStackTrace();
        } catch (
                IllegalAccessException e)

        {
            e.printStackTrace();
        } catch (
                InstantiationException e)

        {
            e.printStackTrace();
        } catch (
                InvocationTargetException e)

        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
