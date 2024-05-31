/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.exception.ParamConfigException;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.data.redis.core.Cursor;
/*     */ import org.springframework.data.redis.core.HashOperations;
/*     */ import org.springframework.data.redis.core.ListOperations;
/*     */ import org.springframework.data.redis.core.RedisTemplate;
/*     */ import org.springframework.data.redis.core.ScanOptions;
/*     */ import org.springframework.data.redis.core.SetOperations;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service
/*     */ public class CacheService {
/*  24 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.CacheService.class);
/*     */   
/*     */   @Autowired
/*     */   private RedisTemplate<String, Object> redisTemplate;
/*     */   
/*     */   @Autowired
/*     */   private HashOperations<String, String, Object> hashOperations;
/*     */   
/*     */   @Autowired
/*     */   private ValueOperations<String, Object> valueOperations;
/*     */   @Autowired
/*     */   private SetOperations<String, Object> setOperations;
/*     */   @Autowired
/*     */   private ListOperations<String, Object> listOperations;
/*  38 */   public static String CACHE_PREFIX = "CACHE";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean expire(String key, long time) {
/*  47 */     if (time > 0L) {
/*  48 */       this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
/*  49 */       return true;
/*     */     } 
/*  51 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getExpire(String key) {
/*  60 */     return this.redisTemplate.getExpire(key, TimeUnit.SECONDS).longValue();
/*     */   }
/*     */   
/*     */   public Set<String> findKeys(String pattern) {
/*  64 */     if (StrUtil.isEmpty(pattern)) {
/*  65 */       pattern = "*";
/*     */     } else {
/*  67 */       pattern = pattern + "*";
/*     */     } 
/*  69 */     int count = 100;
/*  70 */     Set<String> keys = new HashSet<>();
/*  71 */     ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
/*  72 */     Cursor<String> cursor = this.redisTemplate.scan(scanOptions);
/*  73 */     while (cursor.hasNext()) {
/*  74 */       keys.add(cursor.next());
/*     */     }
/*  76 */     return keys;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasKey(String key) {
/*  85 */     return this.redisTemplate.hasKey(key).booleanValue();
/*     */   }
/*     */   
/*     */   private long deleteKeysByPattern(String pattern) {
/*  89 */     return this.redisTemplate.delete(findKeys(pattern)).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long del(String... key) {
/*  97 */     if (key != null && key.length > 0) {
/*  98 */       if (key.length == 1) {
/*  99 */         this.redisTemplate.delete(key[0]);
/*     */       } else {
/* 101 */         this.redisTemplate.delete(CollUtil.toList((Object[])key));
/*     */       } 
/* 103 */       return key.length;
/*     */     } 
/* 105 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue(String key) {
/* 114 */     return StrUtil.isEmpty(key) ? null : this.valueOperations.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putValue(String key, Object value) {
/* 124 */     this.valueOperations.set(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long incr(String key, long delta) {
/* 134 */     if (delta < 0L) {
/* 135 */       throw new RuntimeException("递增因子必须大于0");
/*     */     }
/* 137 */     return this.valueOperations.increment(key, delta).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long decr(String key, long delta) {
/* 147 */     if (delta < 0L) {
/* 148 */       throw new RuntimeException("递减因子必须大于0");
/*     */     }
/* 150 */     return this.valueOperations.increment(key, -delta).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getItem(String key, String item) {
/* 160 */     return this.hashOperations.get(key, item);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getMap(String key) {
/* 169 */     return this.hashOperations.entries(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putMap(String key, Map<String, Object> map) {
/* 179 */     this.hashOperations.putAll(key, map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putItem(String key, String item, Object value) {
/* 190 */     if (value == null) {
/*     */       return;
/*     */     }
/*     */     
/* 194 */     this.hashOperations.put(key, item, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteItem(String key, String... items) {
/* 203 */     this.hashOperations.delete(key, (Object[])items);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hashItem(String key, String item) {
/* 213 */     return this.hashOperations.hasKey(key, item).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double hincr(String key, String item, double by) {
/* 224 */     return this.hashOperations.increment(key, item, by).doubleValue();
/*     */   }
/*     */   
/*     */   public double hincr(String key, String item, Number by) {
/* 228 */     if (by == null) {
/* 229 */       by = Integer.valueOf(0);
/*     */     }
/* 231 */     return this.hashOperations.increment(key, item, by.doubleValue()).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double hdecr(String key, String item, double by) {
/* 242 */     return this.hashOperations.increment(key, item, -by).doubleValue();
/*     */   }
/*     */   
/*     */   public double hdecr(String key, String item, Number by) {
/* 246 */     if (by == null) {
/* 247 */       by = Integer.valueOf(0);
/*     */     }
/* 249 */     return this.hashOperations.increment(key, item, -by.doubleValue()).doubleValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Object> getSet(String key) {
/* 258 */     return this.setOperations.members(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSetValue(String key, Object value) {
/* 268 */     return this.setOperations.isMember(key, value).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long putSetValue(String key, Object... values) {
/* 278 */     return this.setOperations.add(key, values).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSetSize(String key) {
/* 287 */     return this.setOperations.size(key).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long removeSetValue(String key, Object... values) {
/* 297 */     return this.setOperations.remove(key, values).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Object> getList(String key, long start, long end) {
/* 308 */     return this.listOperations.range(key, start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getListSize(String key) {
/* 317 */     return this.listOperations.size(key).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getListValue(String key, long index) {
/* 327 */     return this.listOperations.index(key, index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long addListValue(String key, Object value) {
/* 337 */     return this.listOperations.rightPush(key, value).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long addListValues(String key, List<Object> list) {
/* 347 */     return this.listOperations.rightPushAll(key, list).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setListValue(String key, long index, Object value) {
/* 358 */     this.listOperations.set(key, index, value);
/*     */   }
/*     */   
/*     */   private String getCacheKey(String name, String mainKey, String subKey) {
/* 362 */     if (StrUtil.isEmpty(subKey)) {
/* 363 */       subKey = "0";
/*     */     }
/* 365 */     return DataUtils.generateKey(new String[] { getCacheKey(name, mainKey), subKey });
/*     */   }
/*     */ 
/*     */   
/*     */   private String getCacheKey(String name, String mainKey) {
/* 370 */     if (StrUtil.isEmpty(mainKey)) {
/* 371 */       mainKey = "0";
/*     */     }
/* 373 */     return DataUtils.generateKey(new String[] { getCacheKey(name), mainKey });
/*     */   }
/*     */   
/*     */   private String getCacheKey(String name) {
/* 377 */     if (StrUtil.isEmpty(name)) {
/* 378 */       throw new ParamConfigException("缓存ID不能为空");
/*     */     }
/* 380 */     return DataUtils.generateKey(new String[] { CACHE_PREFIX, name });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMainKey(String cacheKey) {
/* 389 */     String[] keys = cacheKey.split(":");
/* 390 */     return keys[keys.length - 2];
/*     */   }
/*     */   
/*     */   public Set<String> getCacheKeys(String name, String mainKey) {
/* 394 */     return findKeys(getCacheKey(name, mainKey));
/*     */   }
/*     */   
/*     */   public Set<String> getCacheKeys(String name) {
/* 398 */     return findKeys(getCacheKey(name));
/*     */   }
/*     */   
/*     */   public boolean hasCacheKey(String name, String mainKey, String subKey) {
/* 402 */     return hasKey(getCacheKey(name, mainKey, subKey));
/*     */   }
/*     */   
/*     */   public boolean hasCacheKey(String name, String mainKey) {
/* 406 */     return hasKey(getCacheKey(name, mainKey));
/*     */   }
/*     */   
/*     */   public <T> T getCache(String name, String mainKey, String subKey) {
/* 410 */     return (T)getValue(getCacheKey(name, mainKey, subKey));
/*     */   }
/*     */   
/*     */   public <T> T getCache(String name, String mainKey) {
/* 414 */     return (T)getValue(getCacheKey(name, mainKey));
/*     */   }
/*     */   
/*     */   public <T> T getCache(String name) {
/* 418 */     return (T)getValue(getCacheKey(name));
/*     */   }
/*     */   
/*     */   public void putCache(String name, String mainKey, String subKey, Object value) {
/* 422 */     putValue(getCacheKey(name, mainKey, subKey), value);
/*     */   }
/*     */   
/*     */   public void putCache(String name, String mainKey, Object value) {
/* 426 */     putValue(getCacheKey(name, mainKey), value);
/*     */   }
/*     */   
/*     */   public void putCache(String name, Object value) {
/* 430 */     putValue(getCacheKey(name), value);
/*     */   }
/*     */   
/*     */   public void deleteCache(String name, String mainKey, String subKey) {
/* 434 */     del(new String[] { getCacheKey(name, mainKey, subKey) });
/*     */   }
/*     */   
/*     */   public void deleteCache(String name, String mainKey) {
/* 438 */     del(new String[] { getCacheKey(name, mainKey) });
/*     */   }
/*     */   
/*     */   public void deleteCache(String name) {
/* 442 */     del(new String[] { getCacheKey(name) });
/*     */   }
/*     */   
/*     */   public void clearCache(String name, String mainKey) {
/* 446 */     deleteKeysByPattern(getCacheKey(name, mainKey));
/*     */   }
/*     */   
/*     */   public void clearCache(String name) {
/* 450 */     deleteKeysByPattern(getCacheKey(name));
/*     */   }
/*     */   
/*     */   public long increment(String name, long delta) {
/* 454 */     return incr(getCacheKey(name), delta);
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\CacheService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */