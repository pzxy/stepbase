package com.base.common.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * Redis配置
 * @author start
 *
 */
@Configuration
public class RedisClientConfiguration {
	
	@Bean
	@Autowired
	public <K,V>RedisTemplate<K,V> redisTemplate(RedisConnectionFactory connectionFactory,StringRedisTemplate stringRedisTemplate,JdkSerializationRedisSerializer jdkSerializationRedisSerializer) {
		RedisTemplate<K,V> redisTemplate=new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(stringRedisTemplate.getKeySerializer());
		redisTemplate.setHashKeySerializer(stringRedisTemplate.getHashValueSerializer());
		redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
		redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
		return redisTemplate;
	}
	
	@Bean
	@Autowired
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
		StringRedisTemplate redisTemplate=new StringRedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory);
		return redisTemplate;
	}
	
	@Bean
	public JdkSerializationRedisSerializer jdkSerializationRedisSerializer() {
		JdkSerializationRedisSerializer redisTemplate=new JdkSerializationRedisSerializer();
		return redisTemplate;
	}
	
}
