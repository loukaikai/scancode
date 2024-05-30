/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ 
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.exception.ParamConfigException;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class CacheService2
/*     */ {
/*  22 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.CacheService2.class);
/*     */ 
/*     */   
/*  25 */   private Map<String, Object> cacheManagerMap = new ConcurrentHashMap<>();
/*     */   
/*  27 */   public static String CACHE_PREFIX = "CACHE";
/*     */   
/*     */   public Set<String> findKeys(String pattern) {
/*  30 */     if (StrUtil.isEmpty(pattern)) {
/*  31 */       return this.cacheManagerMap.keySet();
/*     */     }
/*  33 */     Set<String> set = new HashSet<>();
/*     */     
/*  35 */     for (String key : this.cacheManagerMap.keySet()) {
/*  36 */       if (key.length() >= pattern.length() && key.substring(0, pattern.length() - 1).equals(pattern)) {
/*  37 */         set.add(key);
/*     */       }
/*     */     } 
/*  40 */     return set;
/*     */   }
/*     */   
/*     */   public boolean hasKey(String key) {
/*  44 */     if (StrUtil.isEmpty(key)) {
/*  45 */       return false;
/*     */     }
/*  47 */     return this.cacheManagerMap.keySet().contains(key);
/*     */   }
/*     */   
/*     */   private long deleteKeysByPattern(String pattern) {
/*  51 */     if (StrUtil.isEmpty(pattern)) {
/*  52 */       return 0L;
/*     */     }
/*  54 */     return deleteKeys(findKeys(pattern));
/*     */   }
/*     */   
/*     */   public long del(String... keys) {
/*  58 */     if (keys.length == 0) {
/*  59 */       return 0L;
/*     */     }
/*  61 */     return deleteKeys(CollUtil.toList((Object[])keys));
/*     */   }
/*     */   
/*     */   private long deleteKeys(Collection<String> keys) {
/*  65 */     if (CollUtil.isEmpty(keys)) {
/*  66 */       return 0L;
/*     */     }
/*  68 */     for (String key : keys) {
/*  69 */       this.cacheManagerMap.remove(key);
/*     */     }
/*  71 */     return keys.size();
/*     */   }
/*     */   
/*     */   private String getCacheKey(String name, String mainKey, String subKey) {
/*  75 */     if (StrUtil.isEmpty(subKey)) {
/*  76 */       subKey = "0";
/*     */     }
/*  78 */     return DataUtils.generateKey(new String[] { getCacheKey(name, mainKey), subKey });
/*     */   }
/*     */   
/*     */   public <T> T getValue(String key) {
/*  82 */     return StrUtil.isEmpty(key) ? null : (T)this.cacheManagerMap.get(key);
/*     */   }
/*     */   
/*     */   public long incr(String key, long delta) {
/*  86 */     if (delta < 0L) {
/*  87 */       throw new RuntimeException("递增因子必须大于0");
/*     */     }
/*  89 */     Long v = getValue(key);
/*  90 */     if (v == null) {
/*  91 */       v = Long.valueOf(delta);
/*     */     } else {
/*  93 */       v = Long.valueOf(v.longValue() + delta);
/*     */     } 
/*  95 */     putValue(key, v);
/*  96 */     return v.longValue();
/*     */   }
/*     */   
/*     */   public void putValue(String key, Object value) {
/* 100 */     this.cacheManagerMap.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   private String getCacheKey(String name, String mainKey) {
/* 105 */     if (StrUtil.isEmpty(mainKey)) {
/* 106 */       mainKey = "0";
/*     */     }
/* 108 */     return DataUtils.generateKey(new String[] { getCacheKey(name), mainKey });
/*     */   }
/*     */   
/*     */   private String getCacheKey(String name) {
/* 112 */     if (StrUtil.isEmpty(name)) {
/* 113 */       throw new ParamConfigException("缓存ID不能为空");
/*     */     }
/* 115 */     return DataUtils.generateKey(new String[] { CACHE_PREFIX, name });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMainKey(String cacheKey) {
/* 124 */     String[] keys = cacheKey.split(":");
/* 125 */     return keys[keys.length - 2];
/*     */   }
/*     */   
/*     */   public Set<String> getCacheKeys(String name, String mainKey) {
/* 129 */     return findKeys(getCacheKey(name, mainKey));
/*     */   }
/*     */   
/*     */   public Set<String> getCacheKeys(String name) {
/* 133 */     return findKeys(getCacheKey(name));
/*     */   }
/*     */   
/*     */   public boolean hasCacheKey(String name, String mainKey, String subKey) {
/* 137 */     return hasKey(getCacheKey(name, mainKey, subKey));
/*     */   }
/*     */   
/*     */   public boolean hasCacheKey(String name, String mainKey) {
/* 141 */     return hasKey(getCacheKey(name, mainKey));
/*     */   }
/*     */   
/*     */   public <T> T getCache(String name, String mainKey, String subKey) {
/* 145 */     return getValue(getCacheKey(name, mainKey, subKey));
/*     */   }
/*     */   
/*     */   public <T> T getCache(String name, String mainKey) {
/* 149 */     return getValue(getCacheKey(name, mainKey));
/*     */   }
/*     */   
/*     */   public <T> T getCache(String name) {
/* 153 */     return getValue(getCacheKey(name));
/*     */   }
/*     */   
/*     */   public void putCache(String name, String mainKey, String subKey, Object value) {
/* 157 */     putValue(getCacheKey(name, mainKey, subKey), value);
/*     */   }
/*     */   
/*     */   public void putCache(String name, String mainKey, Object value) {
/* 161 */     putValue(getCacheKey(name, mainKey), value);
/*     */   }
/*     */   
/*     */   public void putCache(String name, Object value) {
/* 165 */     putValue(getCacheKey(name), value);
/*     */   }
/*     */   
/*     */   public void deleteCache(String name, String mainKey, String subKey) {
/* 169 */     del(new String[] { getCacheKey(name, mainKey, subKey) });
/*     */   }
/*     */   
/*     */   public void deleteCache(String name, String mainKey) {
/* 173 */     del(new String[] { getCacheKey(name, mainKey) });
/*     */   }
/*     */   
/*     */   public void deleteCache(String name) {
/* 177 */     del(new String[] { getCacheKey(name) });
/*     */   }
/*     */   
/*     */   public void clearCache(String name, String mainKey) {
/* 181 */     deleteKeysByPattern(getCacheKey(name, mainKey));
/*     */   }
/*     */   
/*     */   public void clearCache(String name) {
/* 185 */     deleteKeysByPattern(getCacheKey(name));
/*     */   }
/*     */   
/*     */   public long increment(String name, long delta) {
/* 189 */     return incr(getCacheKey(name), delta);
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\CacheService2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */