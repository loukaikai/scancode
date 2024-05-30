/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.SecureRandom;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdWorker
/*     */ {
/*  24 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.util.IdWorker.class);
/*     */ 
/*     */   
/*     */   public static com.amarsoft.rwa.engine.util.IdWorker idWorker;
/*     */ 
/*     */   
/*  30 */   private final long twepoch = 1489111610226L;
/*     */ 
/*     */   
/*  33 */   private final long workerIdBits = 5L;
/*     */ 
/*     */   
/*  36 */   private final long dataCenterIdBits = 5L;
/*     */ 
/*     */   
/*  39 */   private final long maxWorkerId = 31L;
/*     */ 
/*     */   
/*  42 */   private final long maxDataCenterId = 31L;
/*     */ 
/*     */   
/*  45 */   private final long sequenceBits = 12L;
/*     */ 
/*     */   
/*  48 */   private final long workerIdShift = 12L;
/*     */ 
/*     */   
/*  51 */   private final long dataCenterIdShift = 17L;
/*     */ 
/*     */   
/*  54 */   private final long timestampLeftShift = 22L;
/*     */ 
/*     */   
/*  57 */   private final long sequenceMask = 4095L;
/*     */ 
/*     */   
/*     */   private long workerId;
/*     */ 
/*     */   
/*     */   private long dataCenterId;
/*     */ 
/*     */   
/*  66 */   private long sequence = 0L;
/*     */ 
/*     */   
/*  69 */   private long lastTimestamp = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IdWorker(long workerId, long dataCenterId) {
/*  80 */     if (workerId > 31L || workerId < 0L) {
/*  81 */       throw new IllegalArgumentException(
/*  82 */           String.format("workerId can't be greater than %d or less than 0", new Object[] { Long.valueOf(31L) }));
/*     */     }
/*  84 */     if (dataCenterId > 31L || dataCenterId < 0L) {
/*  85 */       throw new IllegalArgumentException(
/*  86 */           String.format("dataCenterId can't be greater than %d or less than 0", new Object[] { Long.valueOf(31L) }));
/*     */     }
/*  88 */     this.workerId = workerId;
/*  89 */     this.dataCenterId = dataCenterId;
/*  90 */     log.info("ID工具初始化[workerId={}][dataCenterId={}]完成", Long.valueOf(workerId), Long.valueOf(dataCenterId));
/*     */   }
/*     */   
/*     */   public IdWorker() {
/*  94 */     int wid = -1;
/*     */     try {
/*  96 */       wid = getStringId(InetAddress.getLocalHost().toString(), 31);
/*  97 */     } catch (UnknownHostException e) {
/*  98 */       log.error("获取服务器信息异常", e);
/*  99 */     } catch (Exception e) {
/* 100 */       log.error("IP计算ID异常", e);
/*     */     } finally {
/* 102 */       if (wid < 0)
/*     */       {
/* 104 */         wid = (new SecureRandom()).nextInt(32);
/*     */       }
/*     */     } 
/*     */     
/* 108 */     int did = (new SecureRandom()).nextInt(32);
/* 109 */     new com.amarsoft.rwa.engine.util.IdWorker(wid, did);
/*     */   }
/*     */   
/*     */   public static int getStringId(String str, int maxId) {
/* 113 */     if (str == null || str.trim().equals(""))
/*     */     {
/* 115 */       return (new SecureRandom()).nextInt(maxId + 1);
/*     */     }
/* 117 */     return Math.abs(str.hashCode() % (maxId + 1));
/*     */   }
/*     */   
/*     */   public static synchronized Long getId() {
/* 121 */     if (idWorker == null) {
/* 122 */       idWorker = new com.amarsoft.rwa.engine.util.IdWorker();
/*     */     }
/* 124 */     return Long.valueOf(idWorker.nextId());
/*     */   }
/*     */   
/*     */   public static synchronized String getIdStr() {
/* 128 */     return getId().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized long nextId() {
/* 137 */     long timestamp = timeGen();
/*     */ 
/*     */     
/* 140 */     if (timestamp < this.lastTimestamp) {
/* 141 */       throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", new Object[] {
/* 142 */               Long.valueOf(this.lastTimestamp - timestamp)
/*     */             }));
/*     */     }
/*     */     
/* 146 */     if (this.lastTimestamp == timestamp) {
/* 147 */       this.sequence = this.sequence + 1L & 0xFFFL;
/*     */       
/* 149 */       if (this.sequence == 0L)
/*     */       {
/* 151 */         timestamp = tilNextMillis(this.lastTimestamp);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 156 */       this.sequence = 0L;
/*     */     } 
/*     */ 
/*     */     
/* 160 */     this.lastTimestamp = timestamp;
/*     */ 
/*     */     
/* 163 */     return timestamp - 1489111610226L << 22L | this.dataCenterId << 17L | this.workerId << 12L | this.sequence;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long tilNextMillis(long lastTimestamp) {
/* 177 */     long timestamp = timeGen();
/* 178 */     while (timestamp <= lastTimestamp) {
/* 179 */       timestamp = timeGen();
/*     */     }
/* 181 */     return timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long timeGen() {
/* 190 */     return System.currentTimeMillis();
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\IdWorker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */