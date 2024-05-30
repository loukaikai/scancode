/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.LockType;
/*    */ import com.amarsoft.rwa.engine.util.DataUtils;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import org.redisson.api.RedissonClient;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.retry.annotation.Backoff;
/*    */ import org.springframework.retry.annotation.Retryable;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class LockService
/*    */ {
/* 22 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.LockService.class);
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private RedissonClient redissonClient;
/*    */   
/* 28 */   public static Map<String, Lock> lockMap = new ConcurrentHashMap<>();
/*    */   
/* 30 */   public static String LOCK_PREFIX = "LOCK";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLockKey(LockType type, String... ids) {
/* 43 */     return DataUtils.generateKey(new String[] { LOCK_PREFIX, type.getCode(), DataUtils.generateKey(ids) });
/*    */   }
/*    */ 
/*    */   
/*    */   @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 1.5D))
/*    */   public Lock getLock(LockType type, String... ids) {
/* 49 */     String key = getLockKey(type, ids);
/* 50 */     return (Lock)this.redissonClient.getLock(key);
/*    */   }
/*    */   
/*    */   @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 1.5D))
/*    */   public Lock getCallApiLock(String... ids) {
/* 55 */     return getLock(LockType.EXE, ids);
/*    */   }
/*    */   
/*    */   @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 1.5D))
/*    */   public Lock getCallApiLock(LockType type, String... ids) {
/* 60 */     return getLock(type, ids);
/*    */   }
/*    */   
/*    */   @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 1.5D))
/*    */   public Lock getProcLock() {
/* 65 */     return getLock(LockType.PROC, new String[] { "0" });
/*    */   }
/*    */   
/*    */   @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 1.5D))
/*    */   public Lock getCacheLock(String... ids) {
/* 70 */     return getLock(LockType.CACHE, ids);
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\LockService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */