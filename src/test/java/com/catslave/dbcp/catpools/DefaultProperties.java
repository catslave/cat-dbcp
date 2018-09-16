package com.catslave.dbcp.catpools;

/**
 * @author cat.uncle
 * @date 2018/9/16
 */
public class DefaultProperties {

    private DataSource dataSource;
    private ConnectionProperties properties;

    public DefaultProperties() {
        properties = new ConnectionProperties();
        properties.setMinIdle(3);
        properties.setMaxWait(5);
    }

    public DataSource getDataSource() {
        if(dataSource == null) {
            dataSource = new DataSource();
            dataSource.setProperties(properties);
        }
        return dataSource;
    }

    public ConnectionProperties getProperties() {
        return properties;
    }
}
