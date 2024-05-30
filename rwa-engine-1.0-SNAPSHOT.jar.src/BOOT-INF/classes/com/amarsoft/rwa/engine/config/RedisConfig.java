/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import org.springframework.cache.annotation.CachingConfigurerSupport;
/*     */ import org.springframework.cache.annotation.EnableCaching;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.data.redis.connection.RedisConnectionFactory;
/*     */ import org.springframework.data.redis.core.HashOperations;
/*     */ import org.springframework.data.redis.core.ListOperations;
/*     */ import org.springframework.data.redis.core.RedisTemplate;
/*     */ import org.springframework.data.redis.core.SetOperations;
/*     */ import org.springframework.data.redis.core.ValueOperations;
/*     */ import org.springframework.data.redis.core.ZSetOperations;
/*     */ import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
/*     */ import org.springframework.data.redis.serializer.RedisSerializer;
/*     */ import org.springframework.data.redis.serializer.StringRedisSerializer;
/*     */ 
/*     */ @Configuration
/*     */ @EnableCaching
/*     */ public class RedisConfig
/*     */   extends CachingConfigurerSupport {
/*     */   @Bean
/*     */   public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
/*  27 */     RedisTemplate<String, Object> template = new RedisTemplate();
/*     */     
/*  29 */     template.setConnectionFactory(factory);
/*     */ 
/*     */     
/*  32 */     Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
/*     */     
/*  34 */     ObjectMapper om = new ObjectMapper();
/*     */     
/*  36 */     om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
/*     */     
/*  38 */     om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
/*  39 */     jacksonSeial.setObjectMapper(om);
/*     */ 
/*     */     
/*  42 */     template.setValueSerializer((RedisSerializer)jacksonSeial);
/*     */     
/*  44 */     template.setKeySerializer((RedisSerializer)new StringRedisSerializer());
/*     */ 
/*     */     
/*  47 */     template.setHashKeySerializer((RedisSerializer)new StringRedisSerializer());
/*  48 */     template.setHashValueSerializer((RedisSerializer)jacksonSeial);
/*  49 */     template.afterPropertiesSet();
/*     */     
/*  51 */     return template;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
/*  62 */     return redisTemplate.opsForHash();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
/*  73 */     return redisTemplate.opsForValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
/*  84 */     return redisTemplate.opsForList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
/*  95 */     return redisTemplate.opsForSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Bean
/*     */   public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
/* 106 */     return redisTemplate.opsForZSet();
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\RedisConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */