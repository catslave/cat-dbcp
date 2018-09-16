package com.catslave.dbcp.catpools;

/**
 * @author cat.uncle
 * @date 2018/9/13
 */
public class ConnectionProperties {

    private volatile int minIdle = 10;
    private volatile int maxWait = 10;

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
}
