package com.catslave.dbcp.catpools;

import org.junit.Test;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author cat.uncle
 * @date 2018/9/13
 */
public class TestDataSource {

    @Test
    public void testDataSource() throws SQLException {
        ConnectionProperties properties = new ConnectionProperties();
        properties.setMinIdle(3);
        properties.setMaxWait(5);

        DataSource dataSource = new DataSource();
        dataSource.setProperties(properties);
        Connection connection = dataSource.getConnection();
        assert connection != null;
        PooledConnection pooledConnection = (PooledConnection) connection;
        Connection connectionActual = pooledConnection.getConnection();
        Statement statement = connectionActual.createStatement();

        assert statement != null;
    }
}
