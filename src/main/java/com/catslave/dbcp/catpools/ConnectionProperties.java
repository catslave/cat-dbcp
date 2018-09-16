package com.catslave.dbcp.catpools;

/**
 * @author cat.uncle
 * @date 2018/9/13
 */
public class ConnectionProperties {

    /**
     * 最小空闲连接数，初始化连接数
     */
    private volatile int minIdle = 10;
    /**
     * 获取连接，最大等待时间
     */
    private volatile int maxWait = 10;
    /**
     * 最大空闲连接数，超过这个数量后就要进去等待
     */
    private volatile int maxActive = 20;
    /**
     * 校验语句
     */
    private volatile String validationQuery = "SELECT 1";


    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }
}
