package com.base.core.aop.service.lock;

/**
 * @author start 
 */
public abstract class AbstractDistributedLockImpl implements IDistributedLock {

    @Override
    public boolean lock(String key,String value) {
        return lock(key ,value , TIMEOUT_MILLIS, RETRY_TIMES, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key,String value, int retryTimes) {
        return lock(key ,value , TIMEOUT_MILLIS, retryTimes, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key,String value, int retryTimes, long sleepMillis) {
        return lock(key ,value , TIMEOUT_MILLIS, retryTimes, sleepMillis);
    }

    @Override
    public boolean lock(String key,String value, long expire) {
        return lock(key ,value , expire, RETRY_TIMES, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key,String value, long expire, int retryTimes) {
        return lock(key ,value , expire, retryTimes, SLEEP_MILLIS);
    }

}

