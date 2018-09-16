package com.catslave.dbcp.catpools;

import com.catslave.dbcp.catpools.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 可池化的连接
 *
 * @author cat.uncle
 * @date 2018/9/13
 */
public class PooledConnection  {

    private final ConnectionPool pool;
    private Connection connection;
    private Driver driver;

    /**
     * Set to true if this connection has been discarded by the pool
     */
    private volatile boolean discarded = false;

    public PooledConnection(ConnectionPool pool) {
        this.pool = pool;
    }

    public void connect() {
        connectUsingDriver();
    }

    public void connectUsingDriver() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/.h2/test;QUERY_TIMEOUT=0;DB_CLOSE_ON_EXIT=FALSE",
                    "root", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void close() throws SQLException {
        // 归还到连接池
        this.pool.returnConnection(this);
    }

    /**
     * release connection and close
     */
    public void release() {
        if(connection != null) {
            try {
                // 还要关闭statement
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public Connection getConnection() {
        return connection;
    }
}
