package com.catslave.dbcp.catpools;

import org.junit.Test;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

/**
 * @author cat.uncle
 * @date 2018/9/13
 */
public class TestDataSource extends DefaultProperties {

    private DataSource dataSource;

    @Test
    public void testDataSource() throws SQLException {

        dataSource = getDataSource();

        Connection connection = dataSource.getConnection();
        assert connection != null;
        Statement connectionStatement = connection.createStatement();


        PooledConnection pooledConnection = (PooledConnection) connection;
        Connection connectionActual = pooledConnection.getConnection();
        Statement statement = connectionActual.createStatement();

        assert statement != null;
    }

    @Test
    public void testValidateQuery() throws SQLException {

        ConnectionProperties properties = getProperties();
        properties.setMinIdle(1);
        properties.setMaxActive(1);
        dataSource = getDataSource();

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select 1");
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        ConnectionProperties properties = getProperties();
        properties.setMinIdle(1);
        properties.setMaxWait(10);
        properties.setMaxActive(1);
        dataSource = getDataSource();

        int curNum = 3;
        final CountDownLatch latch = new CountDownLatch(curNum);

        Thread[] threads = new Thread[curNum];

        for (int i = 0; i < curNum; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Connection connection = dataSource.getConnection();
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("select 1");
                        System.out.println(Thread.currentThread().getName()  + " is done. sleeping some seconds.");
                        Thread.sleep(3 * 1000);
                        resultSet.close();
                        statement.close();
                        System.out.println(Thread.currentThread().getName()  + " return connection.");
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }

                }
            }, "Thread-" + i);
        }

        for (int i = 0; i < curNum; i++) {
            threads[i].start();
        }

        System.out.println("waiting all threads done.");
        latch.await();
        System.out.println("all threads is done.");
    }
}
