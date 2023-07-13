package com.base.core.aop.service.lock;

/**
 * @author start 
 */
public interface IDistributedLock {
	
	static final long TIMEOUT_MILLIS = 5000;

	static final int RETRY_TIMES = Integer.MAX_VALUE;

	static final long SLEEP_MILLIS = 500;

	/**
	 * 分布式锁
	 * @param key
	 * @param value
	 * @return
	 */
	boolean lock(String key,String value);

	/**
	 * 分布式锁
	 * @param key
	 * @param value
	 * @param retryTimes
	 * @return
	 */
	boolean lock(String key,String value, int retryTimes);

	/**
	 * 分布式锁
	 * @param key
	 * @param value
	 * @param retryTimes
	 * @param sleepMillis
	 * @return
	 */
	boolean lock(String key,String value, int retryTimes, long sleepMillis);

	/**
	 * 分布式锁
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	boolean lock(String key,String value, long expire);

	/**
	 * 分布式锁
	 * @param key
	 * @param value
	 * @param expire
	 * @param retryTimes
	 * @return
	 */
	boolean lock(String key,String value, long expire, int retryTimes);

	/**
	 * 分布式锁
	 * @param key
	 * @param value
	 * @param expire
	 * @param retryTimes
	 * @param sleepMillis
	 * @return
	 */
	boolean lock(String key,String value, long expire, int retryTimes, long sleepMillis);

	/**
	 * 分布式锁
	 * @param key
	 * @param value
	 * @return
	 */
	boolean releaseLock(String key,String value);
	
}
