package com.base.core.aop.service.lock;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

/**
 * @author start 
 */
@Component
public class RedisDistributedLock extends AbstractDistributedLockImpl {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDistributedLock.class);

    private RedisTemplate<Object, Object> redisTemplate;

    private static final String LOCK_SUCCESS = "OK";
    private static final RedisScript<String> SCRIPT_LOCK = new DefaultRedisScript<>("return redis.call('set',KEYS[1],ARGV[1],'NX','PX',ARGV[2])", String.class);
    private static final RedisScript<String> SCRIPT_UNLOCK = new DefaultRedisScript<>("if redis.call('get',KEYS[1]) == ARGV[1] then return tostring(redis.call('del', KEYS[1])==1) else return 'false' end", String.class);
    
    public RedisDistributedLock(RedisTemplate<Object, Object> redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean lock(String key,String value, long expire, int retryCount, long sleepMillis) {
        boolean result = setLock(key,value, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while ((!result) && retryCount-- > 0) {
            try {
            	if(LOGGER.isDebugEnabled()) {
            		LOGGER.debug("lock failed, retrying... {}" + retryCount);
            	}
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = setLock(key,value, expire);
        }
        return result;
    }
    
    private boolean setLock(String key,String value, long expire) {
    	Object lockResult = redisTemplate.execute(SCRIPT_LOCK,
                redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(),
                Collections.singletonList(key),
                value, String.valueOf(expire));
        return LOCK_SUCCESS.equals(lockResult);
    }

    /**
     * 解锁
     *
     * <pre>
     * 为何解锁需要校验lockValue
     * 客户端A加锁，一段时间之后客户端A解锁，在执行releaseLock之前，锁突然过期了。
     * 此时客户端B尝试加锁成功，然后客户端A再执行releaseLock方法，则将客户端B的锁给解除了。
     * </pre>
     *
     * @return 是否释放成功
     */
    @Override
    public boolean releaseLock(String key,String value) {
        //释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
    	Object releaseResult = redisTemplate.execute(SCRIPT_UNLOCK,
                redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(),
                Collections.singletonList(key),
                value);
        return Boolean.valueOf(releaseResult.toString());
    }

}

