package com.jzue.study.conf;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.annotation.Resource;

/**
 * @author wiiyaya
 * @date 2019/2/19.
 */
@Configuration
public class RedisConfig {

	@Resource
	private RedisConnectionFactory redisConnectionFactory;

	/**
	 * redis缓存管理模板
	 *
	 * @return RedisTemplate
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
		ObjectMapper objectMapper = jackson2ObjectMapperBuilder.build();

		TypeResolverBuilder<?> defaultTyping = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL);
		defaultTyping = defaultTyping.init(JsonTypeInfo.Id.CLASS, null);
		defaultTyping = defaultTyping.inclusion(JsonTypeInfo.As.PROPERTY);
		objectMapper.setDefaultTyping(defaultTyping);

		StringRedisSerializer keySerializer = new StringRedisSerializer();
		GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setEnableDefaultSerializer(false);
		redisTemplate.setKeySerializer(keySerializer);
		redisTemplate.setValueSerializer(valueSerializer);
		redisTemplate.setHashKeySerializer(keySerializer);
		redisTemplate.setHashValueSerializer(valueSerializer);
		return redisTemplate;
	}
}
