package com.catslave.dbcp.catpools.pool;

import com.catslave.dbcp.catpools.ConnectionProperties;
import com.catslave.dbcp.catpools.PooledConnection;

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

    public PooledConnection borrowConnection() {
        return borrowConnection(properties.getMaxWait());
    }

    public PooledConnection borrowConnection(int wait) {
        PooledConnection connection = null;
        try {
            connection = pool.poll(wait, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void returnConnection(PooledConnection pooledConnection) {
        if (validateConnection(pooledConnection)) {
            pool.offer(pooledConnection);
        }
        pooledConnection.release();
    }

    public boolean validateConnection(PooledConnection connection) {
        return true;
    }

    public void init() {

        // create 10 connections
        for (int i = 0; i < properties.getMinIdle(); i++) {
            PooledConnection pooledConnection = new PooledConnection(this);
            pooledConnection.connect();
            pool.offer(pooledConnection);
        }
    }
}
