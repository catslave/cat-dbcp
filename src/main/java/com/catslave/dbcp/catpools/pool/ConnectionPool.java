package com.catslave.dbcp.catpools.pool;

import com.catslave.dbcp.catpools.ConnectionProperties;
import com.catslave.dbcp.catpools.PooledConnection;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author cat.uncle
 * @date 2018/9/13
 */
public class ConnectionPool {

    private final BlockingQueue<PooledConnection> pool = new LinkedBlockingDeque<>();
    private ConnectionProperties properties;

    public ConnectionPool(ConnectionProperties properties) {
        this.properties = properties;
    }

    public PooledConnection borrowConnection() throws SQLException {
        return borrowConnection(properties.getMaxWait());
    }

    public PooledConnection borrowConnection(int wait) throws SQLException {
        PooledConnection connection = null;
        long beginTime = System.currentTimeMillis();
        try {
            while (true) {
                connection = pool.poll(wait, TimeUnit.SECONDS);
                if (connection != null) {
                    if (validateConnection(connection)) {
                        return connection;
                    } else {
                        // 释放、关闭连接
                        connection.release();
                        return createConnection();
                    }
                }
                // 判断是否超时
                long currentTime = System.currentTimeMillis();
                if ((currentTime - beginTime) > wait * 1000) {
                    throw new SQLException("获取失败");
                }
                // 重新获取连接
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new SQLException(e.getMessage());
        }
        return connection;
    }

    public void returnConnection(PooledConnection pooledConnection) {
        if (validateConnection(pooledConnection)) {
            // 如何校验这个连接是由连接池创建的而非客户直接创建的？
            pool.offer(pooledConnection);
        }
    }

    public boolean validateConnection(PooledConnection connection) {
        return true;
    }

    private PooledConnection createConnection() {
        PooledConnection pooledConnection = new PooledConnection(this);
        pooledConnection.connect();
        return pooledConnection;
    }

    public void init() {

        for (int i = 0; i < properties.getMinIdle(); i++) {
            PooledConnection pooledConnection = createConnection();
            pool.offer(pooledConnection);
        }
    }
}
